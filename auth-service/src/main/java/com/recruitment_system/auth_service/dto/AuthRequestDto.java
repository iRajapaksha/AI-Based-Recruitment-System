package com.recruitment_system.auth_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthRequestDto {
    private String email;
    private String password;
}
