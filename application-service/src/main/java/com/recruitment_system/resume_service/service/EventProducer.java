package com.recruitment_system.resume_service.service;

import com.recruitment_system.event.ApplicationSavedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, ApplicationSavedEvent> applcationSavedKafkaTemplate;

    public void sendApplicationSavedEvent(ApplicationSavedEvent event) {
        applcationSavedKafkaTemplate.send("application-saved-event", event);
        log.info("Published application saved event for postId: " + event.getPostId());
    }


}
