package ru.dponyashov.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.dponyashov.kafkadto.ClientNotify;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {
    @Value("${kafka.topics.mail}")
    private String mailTopicName;

    @Value("${kafka.topics.phone}")
    private String phoneTopicName;

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    @Bean
    public ProducerFactory<Long, ClientNotify> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<Long, ClientNotify> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic mailTopic(){
        return new NewTopic(mailTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic phoneTopic(){
        return new NewTopic(phoneTopicName, 1, (short) 1);
    }
}
