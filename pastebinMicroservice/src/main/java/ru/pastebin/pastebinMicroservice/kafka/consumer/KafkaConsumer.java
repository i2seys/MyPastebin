package ru.pastebin.pastebinMicroservice.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.pastebin.pastebinMicroservice.dto.Paste;
import ru.pastebin.pastebinMicroservice.kafka.KafkaTopic;
import ru.pastebin.pastebinMicroservice.kafka.producer.KafkaProducer;

@Component
@Slf4j
public class KafkaConsumer {
    private final String topicName = "create-paste-topic";// TODO: поискать способы заполнения через конф файл.
    private final String groupId = "group-1";

    private final KafkaProducer kafkaProducer;
    private final KafkaTopic kafkaTopic;

    @Autowired
    public KafkaConsumer(KafkaProducer kafkaProducer, KafkaTopic kafkaTopic) {
        this.kafkaProducer = kafkaProducer;
        this.kafkaTopic = kafkaTopic;
    }

    /**
     * Прослушивает события создания Paste. Когда пришёл запрос, надо запросить Hash с другого микросервиса.
     * */
    @KafkaListener(topics = topicName, groupId = groupId, containerFactory = "pasteKafkaListenerContainerFactory")
    private void createPasteListener(ConsumerRecord<String, Paste> record) {
        Paste paste = record.value();
        log.info("Received CreatePaste message {} in group1", paste);

        log.info("Send to HashMicroservice");
        kafkaProducer.sendGetHashMessage(kafkaTopic.hashTopic().name());
    }
}
