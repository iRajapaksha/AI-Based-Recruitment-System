package com.recruitment_system.notification_service.service;

import com.recruitment_system.event.ConfirmationEmailEvent;
import com.recruitment_system.event.SendVerificationEmailEvent;
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

    // ✅ Consumer for SendVerificationEmailEvent
    @Bean
    public ConsumerFactory<String, SendVerificationEmailEvent> verificationConsumerFactory() {
        JsonDeserializer<SendVerificationEmailEvent> deserializer =
                new JsonDeserializer<>(SendVerificationEmailEvent.class);
        deserializer.addTrustedPackages("com.recruitment_system.event");
        deserializer.ignoreTypeHeaders();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notificationId");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SendVerificationEmailEvent> verificationKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SendVerificationEmailEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(verificationConsumerFactory());
        return factory;
    }

    // ✅ Consumer for ConfirmationEmailEvent
    @Bean
    public ConsumerFactory<String, ConfirmationEmailEvent> confirmationConsumerFactory() {
        JsonDeserializer<ConfirmationEmailEvent> deserializer =
                new JsonDeserializer<>(ConfirmationEmailEvent.class);
        deserializer.addTrustedPackages("com.recruitment_system.event");
        deserializer.ignoreTypeHeaders();

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ConfirmationEmailEvent> confirmationKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ConfirmationEmailEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(confirmationConsumerFactory());
        return factory;
    }
}
