package com.recruitment_system.user_service.service;

import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service @Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileDto getProfile(String email) {
        UserProfile profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return mapToDTO(profile);
    }

    public UserProfileDto updateProfile(String email, UserProfileDto dto) {
        UserProfile profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("User profile not found"));

        if(dto.getFirstname() != null) profile.setFirstname(dto.getFirstname());
        if(dto.getLastname() != null) profile.setLastname(dto.getLastname());
        if(dto.getPhone() != null) profile.setPhone(dto.getPhone());
        if(dto.getProfilePic() != null) profile.setProfilePic(dto.getProfilePic());
        if(dto.getExperience() != null) profile.setExperience(dto.getExperience());
        if(dto.getEducation() != null) profile.setEducation(dto.getEducation());
        if(dto.getBio() != null) profile.setBio(dto.getBio());
        if(dto.getLinkedin() != null) profile.setLinkedin(dto.getLinkedin());
        if(dto.getGithubUrl() != null) profile.setGithubUrl(dto.getGithubUrl());
        if(dto.getResume() != null) profile.setResume(dto.getResume());
        if(dto.getJobTitle() != null) profile.setJobTitle(dto.getJobTitle());
        if(dto.getLocation() != null) profile.setLocation(dto.getLocation());

        System.out.println("Email saved: " + profile.getEmail());
        userProfileRepository.save(profile);
        log.info("Updated profile for {}", email);
        return mapToDTO(profile);
    }

    public List<UserProfileDto> getAllUsers() {
        return userProfileRepository.findAll().stream().map(this::mapToDTO).toList();
    }
    public void createProfile(String email) {
        UserProfile profile1 = UserProfile.builder()
                .email(email)
                .build();
        userProfileRepository.save(profile1);
        log.info("Created profile for {}", email);
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
                .githubUrl(profile.getGithubUrl())
                .resume(profile.getResume())
                .email(profile.getEmail())
                .jobTitle(profile.getJobTitle())
                .location(profile.getLocation())
                .build();
    }
}
