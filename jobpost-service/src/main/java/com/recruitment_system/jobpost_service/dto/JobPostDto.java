package com.recruitment_system.jobpost_service.dto;


import com.recruitment_system.jobpost_service.model.Skill;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class JobPostDto {

    @NotBlank(message = "Company name is required")
    @Size(max = 100, message = "Company name cannot exceed 100 characters")
    private String companyName;

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    @NotBlank(message = "Work type is required")  // e.g., Remote, Hybrid, On-site
    @Pattern(regexp = "Remote|Hybrid|On-site",
            message = "Work type must be Remote, Hybrid, or On-site")
    private String workType;

    @NotBlank(message = "Experience level is required")
    @Pattern(regexp = "Entry|Mid|Senior|Lead",
            message = "Experience level must be Entry, Mid, Senior, or Lead")
    private String experienceLevel;

    @NotBlank(message = "Employment type is required")
    @Pattern(regexp = "Full-time|Part-time|Contract|Internship",
            message = "Employment type must be Full-time, Part-time, Contract, or Internship")
    private String employmentType;

    @NotBlank(message = "Salary is required")
    @Pattern(regexp = "^[0-9]+(\\.[0-9]{1,2})?(-[0-9]+(\\.[0-9]{1,2})?)?$",
            message = "Salary must be a number or a range, e.g. 50000 or 50000-70000")
    private String salary;

    @NotBlank(message = "Job title is required")
    @Size(max = 150, message = "Job title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 20, max = 5000, message = "Description must be between 20 and 5000 characters")
    private String description;

    @NotBlank(message = "Requirements are required")
    @Size(min = 10, max = 3000, message = "Requirements must be between 10 and 3000 characters")
    private String requirements;

    @NotNull(message = "Application deadline is required")
    @Future(message = "Deadline must be a future date")
    private LocalDateTime deadline;

    @NotEmpty(message = "At least one skill is required")
    private List<Skill> skills;

    @NotNull(message = "Organization ID is required")
    private Long orgId;
}
