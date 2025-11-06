package com.recruitment_system.resume_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationDto {

    @NotNull(message = "Post ID is required")
    private Long postId;
    @NotNull
    @Size(min = 1, message = "At least one document must be provided")
    private List<DocumentDto> documentList;
}
