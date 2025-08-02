package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.dto.*;
import com.recruitment_system.auth_service.feign.UserInterface;
import com.recruitment_system.auth_service.model.AuthProvider;
import com.recruitment_system.auth_service.model.Role;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import com.recruitment_system.auth_service.security.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final JavaMailSender javaMailSender;
        private final UserInterface userInterface;



        public RegisterResponseDto register(RegisterRequestDto request) {
                if(userRepository.findByEmail(request.getEmail()).isPresent()){
                        throw new RuntimeException("User already exists with this email");
                }
                String token = UUID.randomUUID().toString(); // or use JWT

                var user = UserEntity.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .authProvider(AuthProvider.LOCAL)
                        .role(Role.UNSET)
                        .isVerified(false)
                        .build();
                userRepository.save(user);
                sendVerificationEmail(user.getEmail(),token);
                userInterface.createProfile(user.getEmail());

                return RegisterResponseDto.builder()
                        .email(user.getEmail())
                        .build();
        }

        public void sendVerificationEmail(String email, String token){
                String link = "http://localhost:8080/api/auth/verify?token=" + token;
                SimpleMailMessage message = new SimpleMailMessage();
               //  message.setFrom("senderemil");
                message.setTo(email);
                message.setSubject("AI Recruitment System Registration");
                message.setText("Welcome to AI recruitment system. Click the link to verify your email: " + link);

                javaMailSender.send(message);


        }

        public ResponseEntity<String> verifyEmail(String token){
                Optional<UserEntity> userOptional = userRepository.findByVerificationToken(token);

                if (userOptional.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token");
                }

                UserEntity user = userOptional.get();
                user.setVerified(true);
                user.setVerificationToken(null); // invalidate token
                userRepository.save(user);

                return ResponseEntity.ok("Email verified successfully! You can now log in.");
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
                // Use when deployed to a server
//                if (!user.isVerified()) {
//                        throw new RuntimeException("Please verify your email before logging in.");
//                }


                UsernamePasswordAuthenticationToken auth =
                         new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

                SecurityContextHolder.getContext().setAuthentication(auth);

                var token = jwtUtil.generateToken(user.getEmail(),user.getRole());
                return new AuthResponseDto(token);
        }
}