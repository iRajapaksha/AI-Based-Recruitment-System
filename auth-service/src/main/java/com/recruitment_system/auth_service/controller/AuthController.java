package com.recruitment_system.auth_service.controller;

import com.recruitment_system.auth_service.dto.*;
import com.recruitment_system.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request) {
       RegisterResponseDto register = authService.register(request);
        return new ResponseEntity<>(register,HttpStatus.OK);
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
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        return authService.login(request);
    }
}
