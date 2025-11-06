package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.event.PostDeadlineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, PostDeadlineEvent> postDeadlineKafkaTemplate;

    public void sendDeadlineEvent(PostDeadlineEvent event) {
        postDeadlineKafkaTemplate.send("post-deadline-event", event);
        log.info("Published post deadline event for postId: " + event.getJobPostId());
    }

}
