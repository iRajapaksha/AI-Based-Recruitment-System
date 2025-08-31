package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.jobpost_service.dto.JobPostDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.model.Skill;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import com.recruitment_system.jobpost_service.repository.SkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostService {
    private final JobPostRepository jobPostRepository;
    private final SkillRepository skillRepository;


    public JobPostResponseDto createJobPost(JobPostDto post) {
        List<Skill> skillEntities = post.getSkills().stream()
                .map(skill -> skillRepository.findByName(skill.getName())
                        .orElseGet(() -> new Skill(skill.getName())))
                .collect(Collectors.toList());
        JobPost jobPost = JobPost.builder()
                .companyName(post.getCompanyName())
                .deadline(post.getDeadline())
                .title(post.getTitle())
                .requirements(post.getRequirements())
                .employmentType(post.getEmploymentType())
                .experienceLevel(post.getExperienceLevel())
                .minSalary(post.getMinSalary())
                .maxSalary(post.getMaxSalary())
                .createdAt(LocalDateTime.now())
                .skills(skillEntities)
                .description(post.getDescription())
                .orgId(post.getOrgId())
                .workType(post.getWorkType())
                .location(post.getLocation())
                .isActive(true)
                .build();
        JobPost saved = jobPostRepository.save(jobPost);

        return mapToResponseDto(saved);
    }

    public List<JobPostResponseDto> getAll() {
        return jobPostRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<JobPostResponseDto> getByOrganization(Long orgId) {
        return jobPostRepository.findByOrgId(orgId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    public JobPostResponseDto deletePostById(Long id) {
        JobPost post = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found with ID: " + id));

            JobPostResponseDto dto = mapToResponseDto(post);
            jobPostRepository.delete(post);
            return dto;
    }


    @Transactional
    public List<JobPostResponseDto> deletePostByOrgId(Long orgId) {
        List<JobPost> jobPosts = jobPostRepository.findByOrgId(orgId);// fetch for return
        List<JobPostResponseDto> dto = jobPosts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        jobPostRepository.deleteAllByOrgId(orgId); // perform deletion
        return dto;
    }

    public JobPostResponseDto updateJobPost(Long postId, Map<String,Object> updates) {
        JobPost post = jobPostRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Job post not found"));
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(JobPost.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, post, value);
            }
        });
        jobPostRepository.save(post);
        return mapToResponseDto(post);
    }

    public JobPostResponseDto getJobPostById(Long postId) {
        JobPost post = jobPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Job post not found with ID: " + postId));
        return mapToResponseDto(post);
    }

    private JobPostResponseDto mapToResponseDto(JobPost post){
        List<Skill> skillEntities = post.getSkills().stream()
                .map(skill -> skillRepository.findByName(skill.getName())
                        .orElseGet(() -> new Skill(skill.getName())))
                .collect(Collectors.toList());

        return JobPostResponseDto.builder()
                .companyName(post.getCompanyName())
                .deadline(post.getDeadline())
                .title(post.getTitle())
                .requirements(post.getRequirements())
                .employmentType(post.getEmploymentType())
                .experienceLevel(post.getExperienceLevel())
                .minSalary(post.getMinSalary())
                .maxSalary(post.getMaxSalary())
                .createdAt(post.getCreatedAt())
                .skills(skillEntities)
                .description(post.getDescription())
                .orgId(post.getOrgId())
                .workType(post.getWorkType())
                .location(post.getLocation())
                .postId(post.getId())
                .isActive(post.getIsActive())
                .build();
    }
}
