package com.recruitment_system.resume_service.service;

import com.recruitment_system.event.ApplicationSavedEvent;
import com.recruitment_system.resume_service.dto.*;
import com.recruitment_system.resume_service.feign.UserInterface;
import com.recruitment_system.resume_service.model.Application;
import com.recruitment_system.resume_service.model.ApplicationDocument;
import com.recruitment_system.resume_service.model.ApplicationStatus;
import com.recruitment_system.resume_service.repository.ApplicationDocumentRepository;
import com.recruitment_system.resume_service.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    private final EventProducer eventProducer;
    private final UserInterface userInterface;

    public ApplicationResponseDto saveApplication(ApplicationDto dto){
        UserProfileDto user = userInterface.getMyProfile().getBody().getData();
        Application application = Application.builder()
                .postId(dto.getPostId())
                .userEmail(user.getEmail())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .githubUrl(user.getGithubUrl())
                .telephone(user.getPhone())
                .address(user.getLocation())
                .appliedAt(LocalDateTime.now())
                .applicationStatus(ApplicationStatus.PENDING)
                .screeningScore(null)
                .interviewScore(null)
                .interviewDate(null)
                .build();
        if (dto.getDocumentList() != null) {
            application.setDocumentList(
                    dto.getDocumentList().stream()
                            .map(doc -> ApplicationDocument.builder()
                                    .documentId(doc.getId())
                                    .type(doc.getType())
                                    .url(doc.getUrl())
                                    .application(application)
                                    .build())
                            .collect(Collectors.toList())
            );
        }
        applicationRepository.save(application);
        log.info("Application saved for  postId: " + application.getPostId());
        eventProducer.sendApplicationSavedEvent(new ApplicationSavedEvent(application.getPostId()));
        return mapToDto(application);
    }

    public List<ApplicationResponseDto> getByPostId(Long postId){
        List<Application> applications = applicationRepository.findByPostId(postId)
                .orElseThrow(()->new RuntimeException("No applications found for post id: "+ postId));
        List<ApplicationResponseDto> dtos = applications.stream()
                .map(this::mapToDto)
                .toList();
        return dtos;
    }

    public List<ApplicationResponseDto> getAllInterviews(String email){
        List<Application> applications =
                applicationRepository.findByUserEmailAndApplicationStatus(
                        email, ApplicationStatus.INTERVIEW_SCHEDULED);
        return applications.stream()
                .map(this::mapToDto)
                .toList();
    }

    public ApplicationResponseDto getById(Long id){
        Application application = applicationRepository.getById(id);
        return mapToDto(application);
    }

    @Transactional
    public List<ApplicationResponseDto> deleteAllByPostId(Long postId){
        List<Application> applications = applicationRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException("No applications found for post id: " + postId));

        List<Long> appIds = applications.stream()
                .map(Application::getId)
                .toList();


        applicationDocumentRepository.deleteByApplicationIdIn(appIds);
        applicationRepository.deleteByPostId(postId);

        return applications.stream()
                .map(this::mapToDto)
                .toList();

    }

    private ApplicationResponseDto mapToDto(Application application){
        return ApplicationResponseDto.builder()
                .applicationId(application.getId())
                .postId(application.getPostId())
                .userEmail(application.getUserEmail())
                .firstName(application.getFirstName())
                .lastName(application.getLastName())
                .githubUrl(application.getGithubUrl())
                .telephone(application.getTelephone())
                .address(application.getAddress())
                .appliedAt(application.getAppliedAt())
                .applicationStatus(application.getApplicationStatus())
                .screeningScore(application.getScreeningScore())
                .interviewScore(application.getInterviewScore())
                .interviewDate(application.getInterviewDate())
                .documentList(application.getDocumentList().stream()
                        .map(doc -> DocumentDto.builder()
                                .id(doc.getDocumentId())
                                .type(doc.getType())
                                .url(doc.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public ApplicationResponseDto setApplicationStatus(Long applicationId, UpdateStatusDto updateStatusDto) {
       Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->new RuntimeException("Application not found for application id: "+ applicationId));

        application.setApplicationStatus(updateStatusDto.getApplicationStatus());
        applicationRepository.save(application);

        log.info("Updated application ID {} to status {}", applicationId,
                updateStatusDto.getApplicationStatus());

        return mapToDto(application);
    }
}
