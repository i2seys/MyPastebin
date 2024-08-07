package ru.pastebin.pastebinMicroservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.pastebin.pastebinMicroservice.deserializer.PasteDeserializer;
import ru.pastebin.pastebinMicroservice.dto.Paste;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaPasteConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final PasteDeserializer pasteDeserializer;

    @Autowired
    public KafkaPasteConsumer(PasteDeserializer pasteDeserializer) {
        this.pasteDeserializer = pasteDeserializer;
    }

    @Bean
    public ConsumerFactory<String, Paste> pasteConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), pasteDeserializer);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Paste>> pasteKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Paste> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pasteConsumerFactory());
        return factory;
    }
}
