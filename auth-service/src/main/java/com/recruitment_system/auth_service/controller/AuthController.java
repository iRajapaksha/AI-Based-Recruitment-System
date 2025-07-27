package com.recruitment_system.auth_service.controller;

import com.recruitment_system.auth_service.dto.AuthRequestDto;
import com.recruitment_system.auth_service.dto.AuthResponseDto;
import com.recruitment_system.auth_service.dto.RegisterRequestDto;
import com.recruitment_system.auth_service.dto.RegisterResponseDto;
import com.recruitment_system.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto request) {
       RegisterResponseDto register = authService.register(request);
        return new ResponseEntity<>(register,HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        return authService.login(request);
    }
}
