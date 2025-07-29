package com.recruitment_system.auth_service.security;

import com.recruitment_system.auth_service.model.AuthProvider;
import com.recruitment_system.auth_service.model.Role;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {


    private  UserRepository userRepository;

    private JwtUtil jwtService;

    public OAuth2LoginSuccessHandler(UserRepository userRepository,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtService =jwtUtil;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String email = user.getAttribute("email");
        Role role = user.getAttribute("role");
        if(userRepository.findByEmail(email).isEmpty()){
            UserEntity newUser = UserEntity.builder()
                    .email(email)
                    .role(Role.UNSET)
                    .authProvider(AuthProvider.GOOGLE)
                    .build();
            userRepository.save(newUser);
        }

        // Generate your custom JWT
        String jwt = jwtService.generateToken(email,role);

        // Optionally store user to DB

        // Redirect to frontend with JWT (or store in cookie)
        response.sendRedirect("http://your-frontend-app.com?token=" + jwt);
    }
}
