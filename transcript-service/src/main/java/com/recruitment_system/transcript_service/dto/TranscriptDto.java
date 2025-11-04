package com.recruitment_system.transcript_service.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDto {
    @NotNull(message = "Application ID is required")
    private Long applicationId;
    @NotNull(message = "Transcript ID is required")
    private String transcriptId;
    @NotBlank(message = "User email is required")
    private String userEmail;
}
