package com.recruitment_system.resume_service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeDto {
    private String fileURI;
    private Long postId;
    private Long userId;
    private LocalDateTime savedAt;
}
