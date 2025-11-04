package com.recruitment_system.transcript_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transcript {

    @GeneratedValue
    @Id
    private Long id;
    private Long applicationId;
    private String transcriptId;
    private String userEmail;
}
