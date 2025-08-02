package com.recruitment_system.user_service.controller;


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
    public ResponseEntity<String> createProfile(@RequestBody String email){

        try {
            service.createProfile(email);
            return ResponseEntity.ok("User profile created successfully.");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }

    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getMyProfile( Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        String email = auth.getName();
        UserProfileDto userProfileDto = service.getProfile(email);
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyProfile(Authentication auth,
                                             @RequestBody Map<String,Object> updates) {

        if (auth == null || !auth.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }
        String email = auth.getName();
        try {
            service.updateProfile(email, updates);
            return ResponseEntity.ok("User profile updated successfully.");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
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
