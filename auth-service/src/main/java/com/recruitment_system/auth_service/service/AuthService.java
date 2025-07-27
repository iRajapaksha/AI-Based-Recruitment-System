package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.dto.AuthRequestDto;
import com.recruitment_system.auth_service.dto.AuthResponseDto;
import com.recruitment_system.auth_service.dto.RegisterRequestDto;
import com.recruitment_system.auth_service.dto.RegisterResponseDto;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import com.recruitment_system.auth_service.security.JWTUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JWTUtility jwtUtility;

        public RegisterResponseDto register(RegisterRequestDto request) {
                if(userRepository.findByEmail(request.getEmail()).isPresent()){
                        throw new RuntimeException("User already exists with this email");
                }
                var user = UserEntity.builder()
                        .username(request.getUsername())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
                userRepository.save(user);

                return RegisterResponseDto.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build();
        }

        public AuthResponseDto login(AuthRequestDto request) {
                UserEntity user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new RuntimeException("Invalid credentials");
                }

                var token = jwtUtility.generateToken(user.getEmail(), user.getRole());
                return new AuthResponseDto(token);
        }
}