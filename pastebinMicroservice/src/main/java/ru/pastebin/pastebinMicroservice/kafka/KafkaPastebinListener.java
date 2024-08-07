package ru.pastebin.pastebinMicroservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.Paste;

@Component
@Slf4j
public class KafkaPastebinListener {
    private final String topicName = "create-paste-topic";// TODO: поискать способы заполнения через конф файл.
    private final String groupId = "group-1";

    @KafkaListener(topics = topicName, groupId = groupId, containerFactory = "pasteKafkaListenerContainerFactory")
    private void listener(ConsumerRecord<String, Paste> record) {
        Paste paste = record.value();
        log.info("Received message [{}] in group1", paste);
    }
}
