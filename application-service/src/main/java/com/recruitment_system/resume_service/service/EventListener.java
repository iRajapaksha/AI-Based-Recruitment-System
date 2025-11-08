package com.recruitment_system.resume_service.service;

import com.recruitment_system.event.SaveScreeningResultEvent;
import com.recruitment_system.event.UpdateInterviewDateEvent;
import com.recruitment_system.resume_service.model.Application;
import com.recruitment_system.resume_service.model.ApplicationStatus;
import com.recruitment_system.resume_service.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {
    private final ApplicationRepository applicationRepository;

    @KafkaListener(topics = "save-screening-result",
            groupId = "screening-result-group",
            containerFactory = "saveScreeningResultKafkaListenerFactory")
    public void saveScreeningResultEvent(SaveScreeningResultEvent event) {
        log.info("Received SaveScreeningResultEvent for candidate email: " + event.getEmail());
        Application application = applicationRepository.findByUserEmailAndPostId(
                event.getEmail(), event.getPostId())
                .orElseThrow(() -> new RuntimeException(
                        "Application not found for email: " + event.getEmail()));
        application.setScreeningScore(event.getScore());
        application.setApplicationStatus(ApplicationStatus.SCREENED);
        applicationRepository.save(application);
        log.info("Saved screening score for applicationId: " + application.getId());

    }

    @KafkaListener(topics = "update-interview-date",
            groupId = "update-interview-date-group",
            containerFactory = "updateInterviewDateKafkaListenerFactory")
    public void updateInterviewDateEvent(UpdateInterviewDateEvent event) {
        log.info("Received UpdateInterviewDateEvent for applicationId: " + event.getApplicationId());
        Application application = applicationRepository.findById(event.getApplicationId())
                .orElseThrow(() -> new RuntimeException(
                        "Application not found for id: " + event.getApplicationId()));
        application.setInterviewDate(event.getInterviewDate());
        application.setApplicationStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applicationRepository.save(application);
        log.info("Updated interview date for applicationId: " + application.getId());
    }
}
