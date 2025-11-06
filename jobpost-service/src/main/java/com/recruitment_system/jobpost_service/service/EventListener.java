package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.event.ApplicationSavedEvent;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventListener {
    private final JobPostRepository jobPostRepository;

    @KafkaListener(topics = "application-saved-event",
            groupId = "jobpost-group",
            containerFactory = "applicationSavedKafkaListenerFactory")
    public void handleApplicationSavedEvent(ApplicationSavedEvent event) {
        log.info("Received application saved event for postId: " + event.getPostId());
        jobPostRepository.findById(event.getPostId()).ifPresent(jobPost -> {
            jobPost.setApplicationsCount(jobPost.getApplicationsCount() + 1);
            jobPostRepository.save(jobPost);
            log.info("Updated application count for postId: " + event.getPostId());
        });
    }
}
