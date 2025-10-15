package com.recruitment_system.jobpost_service.dto;

import com.recruitment_system.jobpost_service.model.Skill;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class JobPostDraftDto {
    private String companyName;
    private String companyLogo;
    private String location;
    private String workType;
    private String experienceLevel;
    private String employmentType;
    private Double minSalary;
    private Double maxSalary;
    private String title;
    private String description;
    private String requirements;
    private String benefits;
    private LocalDateTime deadline;
    private List<Skill> skills;
    private Long orgId;
    private String currency;
}
