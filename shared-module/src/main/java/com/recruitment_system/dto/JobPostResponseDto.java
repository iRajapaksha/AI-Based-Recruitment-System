package com.recruitment_system.dto;

import com.recruitment_system.model.Skill;
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
    private double minSalary;
    private double maxSalary;
    private String title;
    private String description;
    private String requirements;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private List<Skill> skills;
    private Long orgId;


}
