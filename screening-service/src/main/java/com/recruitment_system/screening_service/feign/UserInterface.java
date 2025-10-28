package com.recruitment_system.screening_service.feign;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.screening_service.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile(Authentication auth);
}
