package com.recruitment_system.screening_service.service;

import com.recruitment_system.event.ConfirmationEmailEvent;
import com.recruitment_system.event.SaveScreeningResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, ConfirmationEmailEvent> confirmationEmailKafkaTemplate;
    private final KafkaTemplate<String, SaveScreeningResultEvent> saveScreeningResultKafkaTemplate;

    public void sendConfirmationEmailEvent(ConfirmationEmailEvent event) {
        confirmationEmailKafkaTemplate.send("confirmation-email", event);
        log.info("Published confirmation email event for applicationId: " + event.getApplicationId());
    }
    public void sendSaveScreeningResultEvent(SaveScreeningResultEvent event) {
        saveScreeningResultKafkaTemplate.send("save-screening-result", event);
        log.info("Published save screening result event for candidate: " + event.getEmail());
    }
}
