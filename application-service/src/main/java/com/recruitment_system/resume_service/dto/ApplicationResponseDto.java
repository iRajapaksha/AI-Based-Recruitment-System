package com.recruitment_system.resume_service.dto;

import com.recruitment_system.resume_service.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @Builder
public class ApplicationResponseDto {
    private Long applicationId;
    private Long postId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String githubUrl;
    private String telephone;
    private String address;
    private LocalDateTime appliedAt;
    private List<DocumentDto> documentList;
    private ApplicationStatus applicationStatus;
    private Double screeningScore;
    private Double interviewScore;
}
