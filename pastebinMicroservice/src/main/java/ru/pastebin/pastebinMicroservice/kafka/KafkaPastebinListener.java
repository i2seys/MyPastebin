package ru.pastebin.pastebinMicroservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPastebinListener {
    private final String topicName = "create-paste-topic";// TODO: поискать способы заполнения через конф файл.
    private final String groupId = "group-1";

    @KafkaListener(topics = topicName, groupId = groupId)
    private void listener(String data) {
        log.info("Received message [{}] in group1", data);
    }
}
