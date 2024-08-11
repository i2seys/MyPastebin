package ru.pastebin.hashMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<Long, String> sendHashKafkaTemplate;

    public void sendGenerateHashMessage(String topicName, String hash, Long id) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");

        sendHashKafkaTemplate.send(topicName, id, hash);
    }
}
