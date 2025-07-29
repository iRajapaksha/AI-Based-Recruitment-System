package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.dto.*;
import com.recruitment_system.auth_service.model.AuthProvider;
import com.recruitment_system.auth_service.model.Role;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import com.recruitment_system.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;



        public RegisterResponseDto register(RegisterRequestDto request) {
                if(userRepository.findByEmail(request.getEmail()).isPresent()){
                        throw new RuntimeException("User already exists with this email");
                }
                var user = UserEntity.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .authProvider(AuthProvider.LOCAL)
                        .role(Role.UNSET)
                        .build();
                userRepository.save(user);

                return RegisterResponseDto.builder()
                        .email(user.getEmail())
                        .build();
        }

        public AuthResponseDto setRole(RoleUpdateRequestDto req, Authentication auth){
                String email = auth.getName();
                UserEntity user = userRepository.findByEmail(email).orElseThrow(
                        () -> new RuntimeException("User not found")
                );

                user.setRole(req.getRole());
                userRepository.save(user);
                String  token = jwtUtil.generateToken(user.getEmail(), user.getRole());



                return AuthResponseDto.builder()
                        .token(token)
                        .build();
        }

        public AuthResponseDto login(AuthRequestDto request) {
                UserEntity user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new RuntimeException("Invalid credentials");
                }

                 UsernamePasswordAuthenticationToken auth =
                         new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

                SecurityContextHolder.getContext().setAuthentication(auth);

                var token = jwtUtil.generateToken(user.getEmail(),user.getRole());
                return new AuthResponseDto(token);
        }
}