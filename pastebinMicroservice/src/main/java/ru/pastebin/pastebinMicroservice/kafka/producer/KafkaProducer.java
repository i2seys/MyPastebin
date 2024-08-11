package ru.pastebin.pastebinMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Long> getHashKafkaTemplate;

    public void sendGetHashMessage(Long id, String topicName) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");
        getHashKafkaTemplate.send(topicName, id);
    }
}
