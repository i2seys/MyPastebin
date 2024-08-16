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
    private final String groupId = "group-1";

    private final String getGeneratedHashTopicName = "generated-hash-topic";

    private final KafkaTopic kafkaTopic;

    private final PasteService pasteService;
    private final PasteDeserializer pasteDeserializer;

    @Autowired
    public KafkaConsumer(
            KafkaTopic kafkaTopic,
            PasteDeserializer pasteDeserializer,
            PasteService pasteService
    ) {
        this.kafkaTopic = kafkaTopic;
        this.pasteDeserializer = pasteDeserializer;
        this.pasteService = pasteService;
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
