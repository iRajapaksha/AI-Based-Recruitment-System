package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.event.PostDeadlineEvent;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostDeleteEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobPostDeadlineProducer {
    private final KafkaTemplate<String, PostDeadlineEvent> kafkaTemplate;

    public void sendDeadlineEvent(PostDeadlineEvent event) {
        kafkaTemplate.send("post-deadline-event", event);
        System.out.println("Published deadline event: " + event);
    }
}
