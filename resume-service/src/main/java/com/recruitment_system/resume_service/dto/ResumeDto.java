package com.recruitment_system.resume_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeDto {

    @NotBlank(message = "File URI cannot be blank")
    @Pattern(
            regexp = "^(https?|ftp)://.*$",
            message = "File URI must be a valid URL"
    )
    private String fileURI;

    @NotNull(message = "Post ID cannot be null")
    @Positive(message = "Post ID must be a positive number")
    private Long postId;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @PastOrPresent(message = "Saved time cannot be in the future")
    private LocalDateTime savedAt;
}
