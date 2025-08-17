package com.recruitment_system.organization_service.dto;



import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPostResponseDto {
    private Long postId;
    private String companyName;
    private String location;
    private String workType;
    private String experienceLevel;
    private String employmentType;
    private String salary;
    private String title;
    private String description;
    private String requirements;
    private LocalDateTime deadline;
    private List<Skill> skills;
    private Long orgId;


}
