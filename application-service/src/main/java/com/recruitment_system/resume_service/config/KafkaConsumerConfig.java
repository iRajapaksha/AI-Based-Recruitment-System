package com.recruitment_system.resume_service.config;

import com.recruitment_system.event.PostDeadlineEvent;
import com.recruitment_system.event.SaveScreeningResultEvent;
import com.recruitment_system.event.UpdateInterviewDateEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


     // Generic method to create a ConsumerFactory for any event class.
    private <T> ConsumerFactory<String, T> consumerFactory(Class<T> eventType, String groupId) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(eventType);
        deserializer.addTrustedPackages("com.recruitment_system.event");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    //  Generic method to create a KafkaListenerContainerFactory for any event class.

    private <T> ConcurrentKafkaListenerContainerFactory<String, T>
    kafkaListenerFactory(Class<T> eventType,
                         String groupId) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(eventType, groupId));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SaveScreeningResultEvent>
    saveScreeningResultKafkaListenerFactory() {
        return kafkaListenerFactory(SaveScreeningResultEvent.class,
                "screening-result-group");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UpdateInterviewDateEvent>
    updateInterviewDateKafkaListenerFactory() {
        return kafkaListenerFactory(UpdateInterviewDateEvent.class,
                "update-interview-date-group");
    }
}
