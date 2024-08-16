package ru.pastebin.pastebinMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.serializer.PasteSerializer;
import ru.pastebin.pastebinMicroservice.service.PasteService;

import java.util.List;

@Service
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, Void> getHashKafkaTemplate;
    private final PasteSerializer pasteSerializer;
    private final KafkaTopic kafkaTopic;

    @Autowired
    public KafkaProducer(
            KafkaTemplate<String, Void> getHashKafkaTemplate,
            PasteSerializer pasteSerializer,
            KafkaTopic kafkaTopic
    ) {
        this.getHashKafkaTemplate = getHashKafkaTemplate;
        this.pasteSerializer = pasteSerializer;
        this.kafkaTopic = kafkaTopic;
    }

    public void sendGetHashMessage(Paste paste) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");

        String sendGetHashTopic = kafkaTopic.getHashTopic().name();
        ProducerRecord<String, Void> record = new ProducerRecord<>(
                sendGetHashTopic,
                null,
                System.currentTimeMillis(),
                null,
                null,
                List.of(new RecordHeader("paste", pasteSerializer.serialize(sendGetHashTopic, paste)))
        );
        getHashKafkaTemplate.send(record);
    }
}
