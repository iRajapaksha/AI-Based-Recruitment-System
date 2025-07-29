package com.recruitment_system.user_service.controller;

import com.recruitment_system.user_service.dto.UpdateProfileRequestDto;
import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService service;

    @PostMapping("/create")
    public ResponseEntity<UserProfileDto> createProfile(@RequestBody UserProfileDto profile){

        UserProfileDto userProfileDto = service.createProfile(profile);
        return new ResponseEntity<>(userProfileDto,HttpStatus.OK);

    }

    @GetMapping("/me/{email}")
    public ResponseEntity<UserProfileDto> getMyProfile(@PathVariable String email) {
        UserProfileDto userProfileDto = service.getProfile(email);
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@PathVariable String email,
                                             @RequestBody UpdateProfileRequestDto request) {
        service.updateProfile(email, request);
        return ResponseEntity.ok("Profile updated");
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
