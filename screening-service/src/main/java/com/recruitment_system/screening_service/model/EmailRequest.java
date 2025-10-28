package com.recruitment_system.screening_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {

    @Id
    @GeneratedValue
    private Long id;
    private Long applicationId;
    private Long jobPostId;
    private String email;
    @Column(length=2000)
    private String subject;
    @Column(length=10000)
    private String body;
    private String correlationId;
    @Enumerated(EnumType.STRING)
    private EmailStatus status; // PENDING, SENT, FAILED
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String failureReason;
}
