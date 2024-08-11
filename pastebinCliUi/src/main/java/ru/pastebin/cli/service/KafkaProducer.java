package ru.pastebin.cli.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pastebin.cli.dto.Paste;

@Service
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Paste> kafkaTemplate;

    public void sendCreatePasteMessage(String topicName, Paste paste) {
        log.info("Sending : {}", paste);
        log.info("--------------------------------");

        kafkaTemplate.send(topicName, paste);
    }
}
