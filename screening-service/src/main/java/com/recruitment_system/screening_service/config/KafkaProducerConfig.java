package com.recruitment_system.screening_service.config;


import com.recruitment_system.event.ConfirmationEmailEvent;
import com.recruitment_system.event.SaveScreeningResultEvent;
import com.recruitment_system.event.UpdateInterviewDateEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // Common producer configs
    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return props;
    }

    // ProducerFactory for ConfirmationEmailEvent
    @Bean
    public ProducerFactory<String, ConfirmationEmailEvent>
    confirmationEmailProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ConfirmationEmailEvent>
    confirmationEmailKafkaTemplate() {
        return new KafkaTemplate<>(confirmationEmailProducerFactory());
    }


    // ProducerFactory for SaveScreeningResultEvent
    @Bean
    public ProducerFactory<String, SaveScreeningResultEvent> saveScreeningResultProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    @Bean
    public KafkaTemplate<String, SaveScreeningResultEvent> saveScreeningResultKafkaTemplate() {
        return new KafkaTemplate<>(saveScreeningResultProducerFactory());
    }

    // ProducerFactory for UpdateInterviewDateEvent
    @Bean
    public ProducerFactory<String, UpdateInterviewDateEvent> updateInterviewDateProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }
    @Bean
    public KafkaTemplate<String, UpdateInterviewDateEvent> updateInterviewDateKafkaTemplate() {
        return new KafkaTemplate<>(updateInterviewDateProducerFactory());
    }
}
