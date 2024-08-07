package ru.pastebin.pastebinMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Void> getHashKafkaTemplate;

    public void sendGetHashMessage(String topicName) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");

        getHashKafkaTemplate.send(topicName, null);
    }
}
