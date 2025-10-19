package com.recruitment_system.user_service.controller;


import com.recruitment_system.user_service.dto.ApiResponse;
import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createProfile(@RequestBody String email){
            service.createProfile(email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User profile created successfully.", null));


    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile( Authentication auth) {
        String email = auth.getName();
        UserProfileDto userProfileDto = service.getProfile(email);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully", userProfileDto));

    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(
            Authentication auth,
            @RequestBody UserProfileDto userProfileDto) {


        String email = auth.getName();
        UserProfileDto profile = service.updateProfile(email, userProfileDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully", profile));

    }

    @GetMapping("/all")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return new ResponseEntity<List<UserProfileDto>>(service.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/{email}")
   // @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZATION')")
    public ResponseEntity<UserProfileDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(service.getProfile(email),HttpStatus.OK);
    }
}
