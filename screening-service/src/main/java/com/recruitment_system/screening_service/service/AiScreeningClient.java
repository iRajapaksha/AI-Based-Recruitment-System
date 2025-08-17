package com.recruitment_system.screening_service.service;

import com.recruitment_system.screening_service.dto.ScreeningResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AiScreeningClient {
    private final RestTemplate restTemplate = new RestTemplate();

    public ScreeningResponseDto screenCv(String cvUrl, String jobDescription) {
        Map<String, String> payload = Map.of(
                "cv_url", cvUrl,
                "job_description", jobDescription
        );
        return restTemplate.postForObject(
                "http://python-model-service/screen", payload, ScreeningResponseDto.class
        );
    }
}
