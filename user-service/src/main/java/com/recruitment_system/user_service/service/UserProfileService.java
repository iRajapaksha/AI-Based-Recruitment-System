package com.recruitment_system.user_service.service;

import com.recruitment_system.user_service.dto.UpdateProfileRequestDto;
import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileDto getProfile(String email) {
        UserProfile profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToDTO(profile);
    }

    public UserProfileDto updateProfile(String email, UpdateProfileRequestDto request) {
        var profile = userProfileRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setBio(request.getBio());
        profile.setOrganizationName(request.getOrganizationName());
        profile.setProfilePic(request.getProfilePic());
        profile.setEducation(request.getEducation());
        profile.setQualification(request.getQualification());

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
                .qualification(profile.getQualification())
                .education(profile.getEducation())
                .bio(profile.getBio())
                .organizationName(profile.getOrganizationName())
                .address(profile.getAddress())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .build();


    }

    public UserProfileDto createProfile(UserProfileDto profile) {
        UserProfile profile1 = UserProfile.builder()
                .phone(profile.getPhone())
                .profilePic(profile.getProfilePic())
                .qualification(profile.getQualification())
                .education(profile.getEducation())
                .bio(profile.getBio())
                .organizationName(profile.getOrganizationName())
                .address(profile.getAddress())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .build();

        userProfileRepository.save(profile1);
        return profile;
    }
}
