package com.recruitment_system.screening_service.service;

import com.recruitment_system.dto.JobPostResponseDto;
import com.recruitment_system.screening_service.dto.ResumeResponseDto;
import com.recruitment_system.screening_service.dto.ScreeningResponseDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiScreeningClient {
    private final RestTemplate restTemplate = new RestTemplate();


//    @KafkaListener(topics = "post-deadline-event", groupId = "screening-service")
//    public ScreeningResponseDto screenCv(String cvUrl, String jobDescription) {
//        Map<String, String> payload = Map.of(
//                "cv_url", cvUrl,
//                "job_description", jobDescription
//        );
//        return restTemplate.postForObject(
//                "http://python-model-service/screen", payload, ScreeningResponseDto.class
//        );
//    }

    public ScreeningResponseDto screenCv(JobPostResponseDto jobPost, List<ResumeResponseDto> resumes) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("post_id", jobPost.getPostId());
        payload.put("job_title", jobPost.getTitle());
        payload.put("opening_date", jobPost.getCreatedAt());
        payload.put("deadline", jobPost.getDeadline());
        payload.put("job_description", jobPost.getDescription());
        payload.put("resumes", resumes); // add the list here

        return restTemplate.postForObject(
                "http://python-model-service/screen", payload, ScreeningResponseDto.class
        );
    }
}
