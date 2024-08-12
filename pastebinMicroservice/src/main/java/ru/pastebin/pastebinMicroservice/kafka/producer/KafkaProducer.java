package ru.pastebin.pastebinMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.serializer.PasteSerializer;
import ru.pastebin.pastebinMicroservice.service.PasteService;

import java.util.List;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Void> getHashKafkaTemplate;
    private final PasteSerializer pasteSerializer;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, Void> getHashKafkaTemplate, PasteSerializer pasteSerializer) {
        this.getHashKafkaTemplate = getHashKafkaTemplate;
        this.pasteSerializer = pasteSerializer;
    }

    public void sendGetHashMessage(String topicName, Paste paste) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");

        ProducerRecord<String, Void> record = new ProducerRecord<>(
                topicName,
                null,
                System.currentTimeMillis(),
                null,
                null,
                List.of(new RecordHeader("paste", pasteSerializer.serialize(topicName, paste)))
        );
        getHashKafkaTemplate.send(record);
    }
}
