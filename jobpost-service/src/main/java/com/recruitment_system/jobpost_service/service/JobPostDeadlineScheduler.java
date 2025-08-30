package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostDeadlineScheduler {
    private final JobPostRepository jobPostRepository;
    private final JobPostDeadlineProducer jobPostDeadlineProducer;


    @Scheduled(fixedRate = 60000) // Check every minute
    private void checkDealines(){
        List<Long> expiredJobPostIds = jobPostRepository.findExpiredJobPostIds();
    }
}
