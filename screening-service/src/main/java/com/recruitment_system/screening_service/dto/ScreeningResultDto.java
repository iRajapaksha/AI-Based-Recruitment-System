package com.recruitment_system.screening_service.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ScreeningResultDto {
//    private Long applicationId;
    private String candidate_name;
    private String cv_summary;
    private String github_summary;
    private String email;
    private String match_analysis;
    private double score;

}
