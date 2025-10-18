package com.recruitment_system.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;


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
    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;
    private String linkedin;
    private String experience;
    private String education;
    private String website;
    private String resume;

}
