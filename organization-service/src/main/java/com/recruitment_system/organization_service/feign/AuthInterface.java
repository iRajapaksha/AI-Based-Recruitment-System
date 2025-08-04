package com.recruitment_system.organization_service.feign;

import com.recruitment_system.organization_service.dto.AuthResponseDto;
import com.recruitment_system.organization_service.dto.RoleUpdateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AUTH-SERVICE")
public interface AuthInterface {
    @PostMapping("/auth/set-role")
    public ResponseEntity<AuthResponseDto> setRole(@RequestBody RoleUpdateRequestDto req, Authentication auth);
}
