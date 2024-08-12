package ru.pastebin.pastebinMicroservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.deserializer.PasteDeserializer;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.kafka.producer.KafkaProducer;
import ru.pastebin.pastebinMicroservice.service.PasteService;

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
    private PasteDeserializer pasteDeserializer;

    @Autowired
    public KafkaConsumer(
            KafkaProducer kafkaProducer,
            KafkaTopic kafkaTopic,
            PasteDeserializer pasteDeserializer,
            PasteService pasteService
    ) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaTopic = kafkaTopic;
        this.pasteDeserializer = pasteDeserializer;
        this.pasteService = pasteService;
    }

    /**
     * Прослушивает события создания Paste. Когда пришёл запрос, надо запросить Hash с другого микросервиса.
     * */
    @KafkaListener(topics = createPasteTopicName, groupId = groupId, containerFactory = "pasteKafkaListenerContainerFactory")
    private void createPasteListener(ConsumerRecord<String, Paste> record) {
        Paste paste = record.value();
        log.info("Received CreatePaste message {}, Sending to HashMicroservice", paste);

        kafkaProducer.sendGetHashMessage(kafkaTopic.getHashTopic().name(), paste);
    }

    @KafkaListener(topics = getGeneratedHashTopicName, groupId = groupId, containerFactory = "getGeneratedHashContainerFactory")
    private void getGeneratedHashTopicListener(ConsumerRecord<Void, String> record) {
        String hash = record.value();

        Headers headers = record.headers();
        Header pasteHeader = headers.headers("paste").iterator().next();
        if (pasteHeader == null) {
            log.error("Headers does not contain paste header, cant continue");
            return;
        }
        Paste paste = pasteDeserializer.deserialize(kafkaTopic.generateHashTopic().name(), pasteHeader.value());
        log.info("Paste: {}", paste);

        // Тут надо записать в БД новую запись, а именно - id hash, text varchar, datetime time
        pasteService.savePaste(paste, hash);
    }
}
