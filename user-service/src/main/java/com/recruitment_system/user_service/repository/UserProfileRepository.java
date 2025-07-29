package com.recruitment_system.user_service.repository;

import com.recruitment_system.user_service.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository  extends JpaRepository<UserProfile,String> {

}
