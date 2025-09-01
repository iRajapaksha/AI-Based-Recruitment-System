package com.recruitment_system.screening_service.service;

import com.recruitment_system.dto.JobPostResponseDto;
import com.recruitment_system.screening_service.dto.CandidateResultDto;
import com.recruitment_system.screening_service.dto.ResumeResponseDto;
import com.recruitment_system.screening_service.dto.ScreeningApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class  AiScreeningClient {
    private final RestTemplate restTemplate = new RestTemplate();
    public List<CandidateResultDto> screenedResumes(JobPostResponseDto jobPost, List<ResumeResponseDto> resumes) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("post_id", jobPost.getPostId());
        payload.put("job_title", jobPost.getTitle());
        payload.put("opening_date", jobPost.getCreatedAt());
        payload.put("deadline", jobPost.getDeadline());
        payload.put("job_description", jobPost.getDescription());
        payload.put("resumes", resumes); // add the list here

        ScreeningApiResponseDto response  = restTemplate.postForObject(
                "http://python-model-service/screen", payload, ScreeningApiResponseDto.class
        );

        if (response != null && response.getSuccess() ) {
            return response.getResults();
        } else {
            throw new RuntimeException("Failed to get a valid response from the screening service");
        }
    }
}
