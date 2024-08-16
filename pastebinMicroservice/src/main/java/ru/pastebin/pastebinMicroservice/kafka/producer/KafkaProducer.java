package ru.pastebin.pastebinMicroservice.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.serializer.PasteSerializer;

import java.util.List;
import java.util.UUID;

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

    public void sendGetHashMessage(Paste paste, UUID requestId) {
        log.info("Sending GetHash message");
        log.info("--------------------------------");

        List<Header> headers = List.of(
                new RecordHeader("paste", pasteSerializer.serialize(kafkaTopic.getHashTopic().name(), paste)),
                new RecordHeader("request-id", requestId.toString().getBytes())
        );

        String sendGetHashTopic = kafkaTopic.getHashTopic().name();
        ProducerRecord<String, Void> record = new ProducerRecord<>(
                sendGetHashTopic,
                null,
                System.currentTimeMillis(),
                null,
                null,
                headers
        );
        getHashKafkaTemplate.send(record);
    }
}
