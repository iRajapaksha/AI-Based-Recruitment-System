package com.recruitment_system.resume_service.feign;

import com.recruitment_system.resume_service.dto.ApiResponse;
import com.recruitment_system.resume_service.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile();
}
