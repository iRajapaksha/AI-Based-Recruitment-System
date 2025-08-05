package com.recruitment_system.resume_service.service;

import com.recruitment_system.resume_service.dto.ResumeDto;
import com.recruitment_system.resume_service.dto.ResumeResponseDto;
import com.recruitment_system.resume_service.model.Resume;
import com.recruitment_system.resume_service.repository.ResumeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository resumeRepository;
    public ResumeResponseDto saveResume(ResumeDto dto){
        Resume resume = Resume.builder()
                .fileURI(dto.getFileURI())
                .postId(dto.getPostId())
                .userId(dto.getUserId())
                .savedAt(dto.getSavedAt())
                .build();
        resumeRepository.save(resume);
        return mapToDto(resume);
    }

    public List<ResumeResponseDto> getByPostId(Long postId){
        List<Resume> resumes = resumeRepository.findByPostId(postId)
                .orElseThrow(()->new RuntimeException("No resumes found for post id: "+ postId));
        List<ResumeResponseDto> dtos = resumes.stream()
                .map(this::mapToDto)
                .toList();
        return dtos;
    }

    public ResumeResponseDto getById(Long id){
        Resume resume = resumeRepository.getById(id);
        return mapToDto(resume);
    }

    @Transactional
    public List<ResumeResponseDto> deleteAllByPostId(Long postId){
        List<Resume> resumes = resumeRepository.findByPostId(postId)
                .orElseThrow(()->new RuntimeException("No resumes found for post id: "+ postId));
        List<ResumeResponseDto> dtos = resumes.stream()
                .map(this::mapToDto)
                .toList();
        resumeRepository.deleteByPostId(postId);
        return dtos;

    }

    private ResumeResponseDto mapToDto(Resume resume){
        return ResumeResponseDto.builder()
                .id(resume.getId())
                .fileURI(resume.getFileURI())
                .postId(resume.getPostId())
                .userId(resume.getUserId())
                .savedAt(resume.getSavedAt())
                .build();
    }
}
