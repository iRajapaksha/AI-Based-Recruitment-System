package com.recruitment_system.transcript_service.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponseDto {
    private Long applicationId;
    private String transcriptId;
    private String userEmail;
}
