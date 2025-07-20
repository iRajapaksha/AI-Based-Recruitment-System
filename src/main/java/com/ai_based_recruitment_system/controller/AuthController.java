package com.ai_based_recruitment_system.controller;

import com.ai_based_recruitment_system.dto.AuthResponseDto;
import com.ai_based_recruitment_system.dto.LoginDto;
import com.ai_based_recruitment_system.dto.RegisterDto;
import com.ai_based_recruitment_system.model.Role;
import com.ai_based_recruitment_system.model.UserEntity;
import com.ai_based_recruitment_system.repository.RoleRepository;
import com.ai_based_recruitment_system.repository.UserRepository;
import com.ai_based_recruitment_system.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @GetMapping("/welcome")
    public String welcomeAuth(){
        return "Welcome to auth controller";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>( new AuthResponseDto(token), HttpStatus.OK);

    }

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
            if(userRepository.existsByUsername(registerDto.getUsername())){
                return new ResponseEntity<>("Username is taken!!!", HttpStatus.BAD_REQUEST);
            }
            UserEntity user  = new UserEntity();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setEmail(registerDto.getEmail());

            Role roles = roleRepository.findByName("ROLE_"+registerDto.getRole().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Role not found!"));

            user.setRoles(Collections.singletonList(roles));

            userRepository.save(user);

            return new ResponseEntity<>("User registered succesfully", HttpStatus.OK);
        }


}
