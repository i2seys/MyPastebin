package ru.pastebin.hashMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<Void, String> sendHashKafkaTemplate;

    public void sendGenerateHashMessage(String topicName, String hash, Headers headers) {
        ProducerRecord<Void, String> record = new ProducerRecord<>(
                topicName,
                null,
                System.currentTimeMillis(),
                null,
                hash,
                headers
        );
        sendHashKafkaTemplate.send(record);
    }
}
