package com.recruitment_system.screening_service.service;

import com.recruitment_system.event.ConfirmationEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventProducer {
    private final KafkaTemplate<String, ConfirmationEmailEvent> kafkaTemplate;

    public void sendEmailEvent(ConfirmationEmailEvent event) {
        kafkaTemplate.send("confirmation-email", event);
        System.out.println("Event sent to Kafka: " + event);
    }
}
