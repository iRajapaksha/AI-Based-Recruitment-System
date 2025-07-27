package com.recruitment_system.auth_service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class RegisterResponseDto {
    private String username;
    private String email;
    private String role;
}
