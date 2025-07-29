package com.recruitment_system.user_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class UserProfileDto {
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String bio;
    private String role;
    private String organizationName;
}
