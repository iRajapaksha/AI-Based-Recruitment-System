package com.recruitment_system.screening_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ScreeningResponseDto {
    private Long id;
    private Long resumeId;
    private Long jobPostId;
    private Double score;
    private Integer rank;
    private LocalDateTime createdAt = LocalDateTime.now();
}
