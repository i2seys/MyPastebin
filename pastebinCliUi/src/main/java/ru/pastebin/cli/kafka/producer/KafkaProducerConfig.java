package ru.pastebin.cli.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.pastebin.cli.dto.Paste;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    /**
     * Чтобы начать создавать сообщения, мы сначала настраиваем production factory.
     * Она служит руководством для формирования экземпляров Kafka продюсеров.
     * */
    @Bean
    public ProducerFactory<String, Paste> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // в этом свойстве указываются адреса брокеров Kafka,
        // которые представляют собой список пар хост-порт, разделенных запятыми.
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress
        );

        // Эти свойства определяют, как будут сериализованы ключ и значение сообщения перед отправкой в Kafka.
        // В данном примере мы используем StringSerializer для сериализации как ключа, так и значения.
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class
        );
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Используем шаблон KafkaTemplate,
     * который оборачивается вокруг экземпляра продюсера и предлагает простые методы для отправки сообщений
     * в определенные топики Kafka.
     * */
    @Bean
    public KafkaTemplate<String, Paste> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
