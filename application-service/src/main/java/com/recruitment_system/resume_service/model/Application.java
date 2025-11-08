package com.recruitment_system.resume_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private String userEmail;
    private LocalDateTime appliedAt;
    private String firstName;
    private String lastName;
    private String githubUrl;
    private String telephone;
    private String address;
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplicationDocument> documentList = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    private Double screeningScore;
    private Double interviewScore;
    private LocalDateTime interviewDate;
}
