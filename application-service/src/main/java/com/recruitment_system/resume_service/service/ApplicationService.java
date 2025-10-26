package com.recruitment_system.resume_service.service;

import com.recruitment_system.resume_service.dto.ApplicationDto;
import com.recruitment_system.resume_service.dto.ApplicationResponseDto;
import com.recruitment_system.resume_service.dto.DocumentDto;
import com.recruitment_system.resume_service.model.Application;
import com.recruitment_system.resume_service.model.ApplicationDocument;
import com.recruitment_system.resume_service.repository.ApplicationDocumentRepository;
import com.recruitment_system.resume_service.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationDocumentRepository applicationDocumentRepository;
    public ApplicationResponseDto saveApplication(ApplicationDto dto){
        Application application = Application.builder()
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .appliedAt(LocalDateTime.now())
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
                .userId(application.getUserId())
                .appliedAt(application.getAppliedAt())
                .documentList(application.getDocumentList().stream()
                        .map(doc -> DocumentDto.builder()
                                .id(doc.getDocumentId())
                                .type(doc.getType())
                                .url(doc.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
