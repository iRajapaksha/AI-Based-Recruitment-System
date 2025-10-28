package com.recruitment_system.screening_service.service;

import com.recruitment_system.dto.JobPostResponseDto;
import com.recruitment_system.screening_service.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class  AiScreeningClient {
    private final RestTemplate restTemplate = new RestTemplate();
    public List<ScreeningResultDto> screenedResumes(JobPostResponseDto jobPost,
                                                    List<ApplicationResponseDto> applications) {
        // Outer wrapper (root JSON object)
        Map<String, Object> root = new HashMap<>();

        // Inner "data" object
        Map<String, Object> data = new HashMap<>();

        // Job post info
        Map<String, Object> jobPostMap = new HashMap<>();
        jobPostMap.put("jobTitle", jobPost.getTitle());
        jobPostMap.put("jobDescription", jobPost.getDescription());
        // Convert LocalDateTime to ISO-8601 UTC string
        String closingDateIso = jobPost.getDeadline()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toString();
        // Convert LocalDateTime to ISO-8601 UTC string
        String openingDateIso = jobPost.getDeadline()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toString();
        jobPostMap.put("openingDate", openingDateIso);
        jobPostMap.put("closingDate", closingDateIso);

        // Candidate list
        List<Map<String,Object>> candidateList = new ArrayList<>();

        for(ApplicationResponseDto app : applications) {
            Map<String, Object> candidateMap = getObjectMap(app);
            candidateList.add(candidateMap);
        }

        // Add to data
        data.put("_id", jobPost.getPostId().toString());
        data.put("jobPost", jobPostMap);
        data.put("candidateList", candidateList);

        // Wrap inside root "data"
        root.put("data", data);

        System.out.println("Payload sent to screening service: " + root);

        // Send POST request
        ScreeningApiResponseDto response = restTemplate.postForObject(
                "http://15.235.210.227:8000/trigger_pipeline",
                root,
                ScreeningApiResponseDto.class
        );

        if (response != null && response.getSuccess()) {
            return response.getResults();
        } else {
            throw new RuntimeException("Failed to get a valid response from the screening service");
        }
    }

    private static Map<String, Object> getObjectMap(ApplicationResponseDto app) {
        Map<String,Object> candidateMap = new HashMap<>();
        candidateMap.put("address", app.getAddress());
        candidateMap.put("firstName", app.getFirstName());
        candidateMap.put("lastName", app.getLastName());
        candidateMap.put("telephone", app.getTelephone());
        candidateMap.put("email", app.getUserEmail());
        candidateMap.put("github_url", app.getGithubUrl());

        // Find CV URL from document list
        String cvUrl = null;
        if (app.getDocumentList() != null) {
            for (DocumentDto doc : app.getDocumentList()) {
                if ("cv".equalsIgnoreCase(doc.getType())) {
                    cvUrl = doc.getUrl();
                    break;
                }
            }
        }
        candidateMap.put("cvURL", cvUrl != null ? cvUrl : "");
        return candidateMap;
    }


    public Map<String, EmailContent> generateConfirmationEmails(
            JobPostResponseDto jobPost, List<ApplicationResponseDto> apps) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("jobId", jobPost.getPostId().toString());
        payload.put("jobTitle", jobPost.getTitle());
        payload.put("jobDescription", jobPost.getDescription());

        // Convert LocalDateTime to ISO-8601 UTC string
        String closingDateIso = jobPost.getDeadline()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toString();
        payload.put("closingDate", closingDateIso);

        // Build candidate list as per AI spec
        List<Map<String, String>> candidates = apps.stream()
                .map(app -> {
                    Map<String, String> c = new HashMap<>();
                    c.put("name", app.getFirstName() + " " + app.getLastName());
                    c.put("email", app.getUserEmail());
                    return c;
                })
                .collect(Collectors.toList());
        payload.put("candidates", candidates);

        System.out.println("Payload for AI email generation: " + payload);

        // call AI endpoint to generate emails
        EmailGenerationResponseDto response =
                restTemplate.postForObject(
                        "http://15.235.210.227:8000/generate_emails",
                        payload,
                        EmailGenerationResponseDto.class);

        if (response == null || !response.isSuccess()) {
            throw new RuntimeException("AI email generator failed to respond properly");
        }

        System.out.println("AI email generation response: " + response.getClosing_date()
                + ", emails count: " + response.getEmails().size());

        // Convert emails array → Map<email, EmailContent>
        Map<String, EmailContent> emailMap = new HashMap<>();
        for (EmailGenerationResponseDto.EmailItem item : response.getEmails()) {
            // Split subject from body if AI returns both in "generated_email"
            String fullEmail = item.getGenerated_email();
            System.out.println("Full generated email for " + item.getEmail() + ": " + fullEmail);
            String subject = "Interview Invitation"; // fallback
            String body = fullEmail;

            // Optional: try to extract subject line
            if (fullEmail.startsWith("Subject:")) {
                int splitIndex = fullEmail.indexOf("\n\n");
                if (splitIndex > 0) {
                    subject = fullEmail.substring(8, splitIndex).trim();
                    body = fullEmail.substring(splitIndex + 2).trim();
                }
            }

            emailMap.put(item.getEmail(), new EmailContent(subject, body));
        }

        return emailMap;

    }

}
