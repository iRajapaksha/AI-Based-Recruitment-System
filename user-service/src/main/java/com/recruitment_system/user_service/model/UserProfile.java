package com.recruitment_system.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class UserProfile {
    @Id
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String profilePic;
    private String qualification;
    private String education;
    private String bio;
    private String organizationName;
}
