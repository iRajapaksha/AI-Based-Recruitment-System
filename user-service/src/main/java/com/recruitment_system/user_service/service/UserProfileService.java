package com.recruitment_system.user_service.service;

import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileDto getProfile(String email) {
        UserProfile profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToDTO(profile);
    }

    public UserProfileDto updateProfile(String email, Map<String,Object> updates) {
        UserProfile profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserProfile.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, profile, value);
            }
        });

        userProfileRepository.save(profile);
        return mapToDTO(profile);
    }

    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    private UserProfileDto mapToDTO(UserProfile profile) {
        return UserProfileDto.builder()
                .phone(profile.getPhone())
                .profilePic(profile.getProfilePic())
                .experience(profile.getExperience())
                .education(profile.getEducation())
                .bio(profile.getBio())
                .firstname(profile.getFirstname())
                .lastname(profile.getLastname())
                .linkedin(profile.getLinkedin())
                .website(profile.getWebsite())
                .resume(profile.getResume())
                .email(profile.getEmail())
                .jobTitle(profile.getJobTitle())
                .location(profile.getLocation())
                .build();


    }

    public void createProfile(String email) {
        UserProfile profile1 = UserProfile.builder()
                .email(email)
                .build();
        userProfileRepository.save(profile1);
  //      return profile;
    }
}
