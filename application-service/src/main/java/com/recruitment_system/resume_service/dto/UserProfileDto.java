package com.recruitment_system.resume_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @Builder
public class UserProfileDto {
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
    private String githubUrl;
    private String resume;
}
