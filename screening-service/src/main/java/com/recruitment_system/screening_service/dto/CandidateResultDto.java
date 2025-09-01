package com.recruitment_system.screening_service.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CandidateResultDto {
    private Long resumeId;
    private String candidate_name;
    private String email;
    private String match_analysis;
    private double score;

}
