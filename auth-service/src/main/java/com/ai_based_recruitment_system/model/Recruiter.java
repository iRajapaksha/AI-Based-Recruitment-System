package com.ai_based_recruitment_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Recruiters")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
