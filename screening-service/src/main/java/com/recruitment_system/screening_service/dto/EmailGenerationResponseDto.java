package com.recruitment_system.screening_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailGenerationResponseDto {
    private boolean success;
    private String job_id;
    private String job_title;
    private String closing_date;
    private String interview_date;
    private List<EmailItem> emails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailItem {
        private String candidate_name;
        private String email;
        private String generatedEmail;
    }
}
