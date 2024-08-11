package ru.pastebin.pastebinMicroservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaTopic {
    @Value("${spring.kafka.get_hash_topic_name}")
    private String getHashTopicName;
    @Value("${spring.kafka.generated_hash_topic_name}")
    private String generateHashTopicName;
    @Bean
    public NewTopic getHashTopic() {
        return TopicBuilder.name(getHashTopicName).build();
    }
    @Bean
    public NewTopic generateHashTopic() {
        return TopicBuilder.name(generateHashTopicName).build();
    }
}
