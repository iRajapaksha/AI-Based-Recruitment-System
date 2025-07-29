package com.recruitment_system.auth_service.dto;

import com.recruitment_system.auth_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class RoleUpdateRequestDto {
    Role role;
}
