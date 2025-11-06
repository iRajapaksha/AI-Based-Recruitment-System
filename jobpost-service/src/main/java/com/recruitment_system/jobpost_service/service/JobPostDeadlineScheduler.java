package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.event.PostDeadlineEvent;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostDeadlineScheduler {
    private final JobPostRepository jobPostRepository;
    private final EventProducer eventProducer;


    @Scheduled(fixedRate = 60000*60) // Check every minute
    public void checkDealines(){
        List<Long> expiredJobPostIds = jobPostRepository.findByDeadlineBeforeAndIsActive(LocalDateTime.now(),true)
                .stream()
                .map(JobPost::getId)
                .toList();
        System.out.println("Expired job posts: " + expiredJobPostIds);
        for(Long jobPostId : expiredJobPostIds){
        PostDeadlineEvent event = new PostDeadlineEvent(jobPostId);
        eventProducer.sendDeadlineEvent(event);
        JobPost jobPost = jobPostRepository.findById(jobPostId).orElseThrow();
        jobPost.setIsActive(false); // mark as inactive
            jobPostRepository.save(jobPost);
        }
    }
}
