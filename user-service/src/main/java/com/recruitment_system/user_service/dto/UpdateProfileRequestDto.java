package com.recruitment_system.user_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UpdateProfileRequestDto {
    private String fullName;
    private String phone;
    private String address;
    private String profilePic;
    private String qualification;
    private String education;
    private String bio;
    private String organizationName;
}
