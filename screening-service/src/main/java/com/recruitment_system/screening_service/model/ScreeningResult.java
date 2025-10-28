package com.recruitment_system.screening_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreeningResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jobPostId;
    private Long applicationId;
    private String candidate_name;
    private String email;
    @Lob @Column(columnDefinition = "TEXT")
    private String match_analysis;
    private double score;
}
