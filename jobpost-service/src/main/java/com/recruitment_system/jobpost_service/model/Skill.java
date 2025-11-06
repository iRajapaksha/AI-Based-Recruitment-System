package com.recruitment_system.jobpost_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "skill")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    public Skill(String name) {
        this.name = name;
    }
}
