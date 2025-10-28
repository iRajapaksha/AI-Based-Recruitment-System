package com.recruitment_system.resume_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationDto {

    @NotNull(message = "Post ID is required")
    private Long postId;

    private List<DocumentDto> documentList;
}
