package ru.pastebin.cli.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@PropertySource("classpath:application.properties")
public class KafkaTopic {

    @Value("${spring.kafka.create_paste_topic_name}")
    private String topicName;
    @Bean
    public NewTopic mainTopic() {
        return TopicBuilder.name(topicName).build();
    }
}
