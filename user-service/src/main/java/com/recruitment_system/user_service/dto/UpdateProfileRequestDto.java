package com.recruitment_system.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateProfileRequestDto {
    private String fullName;
    private String phone;
    private String address;
    private String bio;
    private String organizationName;
}
