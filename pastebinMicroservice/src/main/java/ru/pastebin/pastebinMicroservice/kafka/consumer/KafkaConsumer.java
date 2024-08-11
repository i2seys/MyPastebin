package ru.pastebin.pastebinMicroservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.kafka.producer.KafkaProducer;
import ru.pastebin.pastebinMicroservice.service.PasteService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Slf4j
public class KafkaConsumer {

    private final String createPasteTopicName = "create-paste-topic";// TODO: поискать способы заполнения через конф файл.
    private final String groupId = "group-1";

    private final String getGeneratedHashTopicName = "generated-hash-topic";

    private final KafkaProducer kafkaProducer;
    private final KafkaTopic kafkaTopic;

    private PasteService pasteService;

    /** Нужен для отслеживания запроса. */
    private final AtomicLong requestId;
    /** Нужна для связки id и пасты */
    private final ConcurrentMap<Long, Paste> idToTime;

    @Autowired
    public KafkaConsumer(
            KafkaProducer kafkaProducer,
            KafkaTopic kafkaTopic,
            @Value("${cache.request_hash_cache_size}") int idToTimeSize,
            PasteService pasteService
    ) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaTopic = kafkaTopic;
        this.requestId = new AtomicLong();
        this.idToTime = new ConcurrentHashMap<>(idToTimeSize);
        this.pasteService = pasteService;
    }

    /**
     * Прослушивает события создания Paste. Когда пришёл запрос, надо запросить Hash с другого микросервиса.
     * */
    @KafkaListener(topics = createPasteTopicName, groupId = groupId, containerFactory = "pasteKafkaListenerContainerFactory")
    private void createPasteListener(ConsumerRecord<String, Paste> record) {
        Paste paste = record.value();
        log.info("Received CreatePaste message {}", paste);

        log.info("Send to HashMicroservice");

        // Для того, чтобы в будущем знать, какое время принадлежало пришедшему Hash,
        // требуется хранить эту информацию в памяти.
        synchronized (this) {
            idToTime.put(requestId.getAndAdd(1), paste);
            kafkaProducer.sendGetHashMessage(requestId.get() - 1, kafkaTopic.getHashTopic().name());
        }
    }

    @KafkaListener(topics = getGeneratedHashTopicName, groupId = groupId, containerFactory = "getGeneratedHashContainerFactory")
    private void getGeneratedHashTopicListener(ConsumerRecord<Long, String> record) {
        String hash = record.value();
        Long id = record.key();
        if (!idToTime.containsKey(id)) {
            log.error("Cache does not contains this id: {}, hash: {}", id, hash);
            return;
        }
        Paste paste = idToTime.get(id);

        idToTime.remove(id);
        log.info("Save paste: {} {}", paste, hash);
        // Тут надо записать в БД новую запись, а именно - id hash, text varchar, datetime time
        pasteService.savePaste(paste, hash);
    }
}
