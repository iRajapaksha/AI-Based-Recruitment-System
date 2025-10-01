package com.recruitment_system.screening_service.service;

import com.recruitment_system.dto.JobPostResponseDto;
import com.recruitment_system.screening_service.dto.CandidateResultDto;
import com.recruitment_system.screening_service.dto.ResumeResponseDto;
import com.recruitment_system.screening_service.dto.ScreeningApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
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
        payload.put("job_description", jobPost.getDescription());
        payload.put("resumes", resumes);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        payload.put("deadline", jobPost.getDeadline().format(formatter));
        payload.put("opening_date", jobPost.getCreatedAt().format(formatter));

        ScreeningApiResponseDto response  = restTemplate.postForObject(
                "http://localhost:5000/screen", payload, ScreeningApiResponseDto.class
        );

        if (response != null && response.getSuccess() ) {
            return response.getResults();
        } else {
            throw new RuntimeException("Failed to get a valid response from the screening service");
        }
    }
}
