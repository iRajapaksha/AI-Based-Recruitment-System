package com.recruitment_system.jobpost_service.config;

import com.recruitment_system.event.ApplicationSavedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    private final String bootstrapServers = "localhost:9092";

    @Bean
    public ConsumerFactory<String, ApplicationSavedEvent> applicationSavedConsumerFactory() {
        JsonDeserializer<ApplicationSavedEvent> deserializer =
                new JsonDeserializer<>(ApplicationSavedEvent.class);
        deserializer.addTrustedPackages("com.recruitment_system.event");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "jobpost-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApplicationSavedEvent>
    applicationSavedKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApplicationSavedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(applicationSavedConsumerFactory());
        return factory;
    }
}
