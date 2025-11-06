package com.recruitment_system.auth_service.service;

import com.recruitment_system.event.SendVerificationEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, SendVerificationEmailEvent> sendVerificationEmailKafkaTemplate;

    public void sendVerificationEmailEvent(SendVerificationEmailEvent event){
        log.info("Producing SendVerificationEmailEvent for email: {}", event.getEmail());
        sendVerificationEmailKafkaTemplate.send("notification", event);
    }

}
