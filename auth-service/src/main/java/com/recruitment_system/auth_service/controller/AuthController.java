package com.recruitment_system.auth_service.controller;

import com.recruitment_system.auth_service.dto.*;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> register(@Valid @RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "User registered successfully.", authService.register(request))
        );
    }

    @PostMapping("/set-role")
    public ResponseEntity<AuthResponseDto> setRole(@RequestBody RoleUpdateRequestDto req, Authentication auth)
    {

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("User not authenticated");
        }

        AuthResponseDto response = authService.setRole(req,auth);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "User login successfully.", authService.login(request))
        );
    }
    @GetMapping("/verify/{token}")
    public ResponseEntity<ApiResponse<String>> verifyUser(@PathVariable String token) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Email verified successfully.", authService.verifyEmail(token))
        );
    }

}
