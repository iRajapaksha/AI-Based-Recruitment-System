package com.recruitment_system.jobpost_service.dto;


import com.recruitment_system.jobpost_service.model.Skill;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class JobPostDto {
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
