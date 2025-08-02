package com.recruitment_system.user_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class UserProfile {
    @Id
    private String email;
    private String firstname;
    private String lastname;
    private String location;
    private String phone;
    private String profilePic;
    private String jobTitle;
    private String bio;
    private String linkedin;
    private String experience;
    private String education;
    private String website;
    private String resume;

}
