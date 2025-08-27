package com.recruitment_system.jobpost_service.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private Long orgId;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "job_post_skills",
            joinColumns = @JoinColumn(name = "job_post_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

}
