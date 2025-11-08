package com.recruitment_system.user_service.repository;

import com.recruitment_system.user_service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository  extends JpaRepository<UserProfile,String> {
    Optional<UserProfile> findByEmail(String email);
}
