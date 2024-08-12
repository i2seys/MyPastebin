package ru.pastebin.hashMicroservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.hashMicroservice.kafka.KafkaTopic;
import ru.pastebin.hashMicroservice.kafka.producer.KafkaProducer;
import ru.pastebin.hashMicroservice.service.HashGenerator;

@Slf4j
@Component
public class KafkaConsumer {
    private final String topicName = "get-hash-topic";// TODO: поискать способы заполнения через конф файл.
    private final String groupId = "group-1";

    private final HashGenerator hashGenerator;
    private final KafkaTopic kafkaTopic;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public KafkaConsumer(KafkaTopic kafkaTopic, HashGenerator hashGenerator, KafkaProducer kafkaProducer) {
        this.kafkaTopic = kafkaTopic;
        this.hashGenerator = hashGenerator;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Прослушивает события создания Paste. Когда пришёл запрос, надо запросить Hash с другого микросервиса.
     * */
    @KafkaListener(topics = topicName, groupId = groupId, containerFactory = "hashKafkaListenerContainerFactory")
    private void createPasteListener(ConsumerRecord<Void, Void> record) {
        log.info("Received GetHash message.");
        String hash = hashGenerator.generateAndSaveHash();
        log.info("Hash: {}", hash);

        kafkaProducer.sendGenerateHashMessage(kafkaTopic.generateHashTopic().name(), hash, record.headers());
    }
}
