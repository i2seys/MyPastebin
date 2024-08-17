package ru.pastebin.pastebinMicroservice.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.VoidDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import ru.pastebin.pastebinMicroservice.deserializer.PasteDeserializer;
import ru.pastebin.pastebinMicroservice.dto.PasteRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final PasteDeserializer pasteDeserializer;

    @Autowired
    public KafkaConsumerConfig(PasteDeserializer pasteDeserializer) {
        this.pasteDeserializer = pasteDeserializer;
    }

    @Bean
    public ConsumerFactory<String, PasteRequest> pasteConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), pasteDeserializer);
    }

    @Bean
    public ConsumerFactory<Void, String> getGeneratedHashConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        return new DefaultKafkaConsumerFactory<>(props, new VoidDeserializer(), new StringDeserializer());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Void, String>> getGeneratedHashContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Void, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(getGeneratedHashConsumerFactory());
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, PasteRequest>> pasteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PasteRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pasteConsumerFactory());
        return factory;
    }
}
