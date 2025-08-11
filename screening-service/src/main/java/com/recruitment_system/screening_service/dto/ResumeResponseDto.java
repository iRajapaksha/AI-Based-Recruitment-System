package com.recruitment_system.resume_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @Builder
public class ResumeResponseDto {
    private Long id;
    private String fileURI;
    private Long postId;
    private Long userId;
    private LocalDateTime savedAt;
}
