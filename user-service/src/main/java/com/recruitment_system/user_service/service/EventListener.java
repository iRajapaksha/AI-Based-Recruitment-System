package com.recruitment_system.user_service.service;

import com.recruitment_system.event.CreateUserProfileEvent;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {

    private final UserProfileRepository userProfileRepository;

    @KafkaListener(topics = "user-profile",
            groupId = "user-service-group",
            containerFactory = "createUserProfileConsumerFactory")
    public void createUserProfileEvent(CreateUserProfileEvent event){
        log.info("Received CreateUserProfileEvent for email: {}", event.getEmail());

        if(userProfileRepository.findByEmail(event.getEmail()).isPresent()){
            log.info("User profile already exists for email: {}", event.getEmail());
            return;
        }
        var userProfile = UserProfile.builder()
                .email(event.getEmail())
                .build();
        userProfileRepository.save(userProfile);
        log.info("User profile created for email: {}", event.getEmail());
    }

}
