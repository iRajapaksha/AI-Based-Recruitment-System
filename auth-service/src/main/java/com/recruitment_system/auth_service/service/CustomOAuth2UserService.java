package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.model.AuthProvider;
import com.recruitment_system.auth_service.model.UserEntity;
import com.recruitment_system.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);
        String email = user.getAttribute("email");

        Optional<UserEntity> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            return new CustomOauthUser(existing.get());
        }

        // Register new user with role = null
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setAuthProvider(AuthProvider.GOOGLE);
        newUser.setRole(null); // user must select role next

        userRepository.save(newUser);
        return new CustomOauthUser(newUser);

        // Extract needed info

        // Optionally create user in DB if not exist
        // Also trigger profile creation in User-Service via REST

    }
}
