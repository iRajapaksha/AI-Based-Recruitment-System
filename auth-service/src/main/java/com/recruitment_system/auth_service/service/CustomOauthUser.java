package com.recruitment_system.auth_service.service;

import com.recruitment_system.auth_service.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOauthUser implements OAuth2User {
    private final UserEntity user;
    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("email", user.getEmail(), "role", user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + (user.getRole() == null ? "UNSET" : user.getRole().name()));

    }


    @Override
    public String getName() {
        return user.getEmail();
    }
}
