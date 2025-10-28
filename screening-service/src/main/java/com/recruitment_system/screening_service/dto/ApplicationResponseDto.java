package com.recruitment_system.screening_service.dto;

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
}
