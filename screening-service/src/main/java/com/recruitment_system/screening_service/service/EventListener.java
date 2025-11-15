package com.recruitment_system.screening_service.service;

import com.recruitment_system.event.PostDeadlineEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {
    private final ScreeningService screeningService;

    @KafkaListener(topics = "post-deadline-event",
            groupId = "screening-service-group",
            containerFactory = "postDeadlineKafkaListenerFactory")
    public void screenJobPost(PostDeadlineEvent event) {
        Long jobPostId = event.getJobPostId();
        try {
            System.out.println("Received Kafka event for job post ID: " + jobPostId);
            screeningService.processScreening(jobPostId);
        } catch (Exception ex) {
            System.err.println("Error processing screening for job post ID " +
                    jobPostId + ": " + ex.getMessage());
            throw new RuntimeException("Screening failed: " + ex.getMessage(), ex);

        }

    }

}
