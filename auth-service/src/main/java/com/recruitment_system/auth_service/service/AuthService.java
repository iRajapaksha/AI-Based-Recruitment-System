package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.dto.*;
import com.recruitment_system.auth_service.feign.UserInterface;
import com.recruitment_system.auth_service.model.AuthProvider;
import com.recruitment_system.auth_service.model.Role;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import com.recruitment_system.auth_service.security.JwtUtil;
import com.recruitment_system.event.CreateUserProfileEvent;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.recruitment_system.event.SendVerificationEmailEvent;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final UserInterface userInterface;
        private final EventProducer eventProducer;



        public RegisterResponseDto register(RegisterRequestDto request) {
                if(userRepository.findByEmail(request.getEmail()).isPresent()){
                        throw new RuntimeException("User already exists with this email." +
                                "Please verify your email. Check inbox.");
                }
                String token = UUID.randomUUID().toString(); // or use JWT

                var user = UserEntity.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .authProvider(AuthProvider.LOCAL)
                        .verificationToken(token)
                        .role(Role.USER)
                        .isVerified(false)
                        .build();
                userRepository.save(user);
                log.info("User registered with email: " + user.getEmail());
                eventProducer.sendVerificationEmailEvent(new SendVerificationEmailEvent(user.getEmail(), token));
                eventProducer.createUserProfileEvent(new CreateUserProfileEvent(user.getEmail()));

                return RegisterResponseDto.builder()
                        .email(user.getEmail())
                        .build();
        }


        public String verifyEmail(String token){
                Optional<UserEntity> userOptional = userRepository.findByVerificationToken(token);

                if (userOptional.isEmpty()) {
                        return "Invalid or expired token";
                }

                UserEntity user = userOptional.get();
                user.setVerified(true);
                user.setVerificationToken(null); // invalidate token
                userRepository.save(user);
                // for successful verification.add redirection to login page
                return "Email verified successfully! You can now log in.";
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
                        .email(user.getEmail())
                        .build();
        }

        public AuthResponseDto login(AuthRequestDto request) {
                UserEntity user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new RuntimeException("Invalid credentials");
                }
                // Use when deployed to a server. put password check inside verification
//                if (!user.isVerified()) {
//                        throw new RuntimeException("Please verify your email before log in.");
//                }


                UsernamePasswordAuthenticationToken auth =
                         new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

                SecurityContextHolder.getContext().setAuthentication(auth);

                var token = jwtUtil.generateToken(user.getEmail(),user.getRole());
                return new AuthResponseDto(token, user.getEmail());
        }
}