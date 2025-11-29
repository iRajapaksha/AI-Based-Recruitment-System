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
    private String companyLogo;
    private String location;
    private String workType;
    private String experienceLevel;
    private String employmentType;
    private Double minSalary;
    private Double maxSalary;
    private Boolean isActive;
    private Boolean isDraft;
    private String title;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String requirements;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String benefits;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private int applicationsCount = 0;
    private Long orgId;
    private String createdBy;
    private String currency;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "job_post_skills",
            joinColumns = @JoinColumn(name = "job_post_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

}
