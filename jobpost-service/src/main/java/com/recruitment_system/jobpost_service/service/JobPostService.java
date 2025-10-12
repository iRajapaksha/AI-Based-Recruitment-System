package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.jobpost_service.dto.JobPostDraftDto;
import com.recruitment_system.jobpost_service.dto.JobPostDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.model.Skill;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import com.recruitment_system.jobpost_service.repository.SkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .isDraft(false)
                .build();
        JobPost saved = jobPostRepository.save(jobPost);

        return mapToResponseDto(saved);
    }

    public JobPostResponseDto saveDraft(JobPostDraftDto draft) {
        JobPost jobPost = JobPost.builder()
                .companyName(draft.getCompanyName())
                .location(draft.getLocation())
                .workType(draft.getWorkType())
                .experienceLevel(draft.getExperienceLevel())
                .employmentType(draft.getEmploymentType())
                .minSalary(draft.getMinSalary() != null ? draft.getMinSalary() : 0)
                .maxSalary(draft.getMaxSalary() != null ? draft.getMaxSalary() : 0)
                .title(draft.getTitle())
                .description(draft.getDescription())
                .requirements(draft.getRequirements())
                .deadline(draft.getDeadline())
                .orgId(draft.getOrgId())
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .isDraft(true)
                .skills(draft.getSkills() != null ? draft.getSkills() : new ArrayList<Skill>())
                .build();

        JobPost saved = jobPostRepository.save(jobPost);
        return mapToResponseDto(saved);
    }

    public JobPostResponseDto publishDraft(Long draftId, JobPostDto completePost) {
        JobPost existing = jobPostRepository.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));

        existing.setCompanyName(completePost.getCompanyName());
        existing.setLocation(completePost.getLocation());
        existing.setWorkType(completePost.getWorkType());
        existing.setExperienceLevel(completePost.getExperienceLevel());
        existing.setEmploymentType(completePost.getEmploymentType());
        existing.setMinSalary(completePost.getMinSalary());
        existing.setMaxSalary(completePost.getMaxSalary());
        existing.setTitle(completePost.getTitle());
        existing.setDescription(completePost.getDescription());
        existing.setRequirements(completePost.getRequirements());
        existing.setDeadline(completePost.getDeadline());
        existing.setOrgId(completePost.getOrgId());
        existing.setIsActive(true);
        existing.setIsDraft(false);
        existing.setCreatedAt(LocalDateTime.now());

        List<Skill> skillEntities = completePost.getSkills().stream()
                .map(skill -> skillRepository.findByName(skill.getName())
                        .orElseGet(() -> new Skill(skill.getName())))
                .collect(Collectors.toList());

        existing.setSkills(skillEntities);

        JobPost saved = jobPostRepository.save(existing);
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
                .isDraft(post.getIsDraft())
                .build();
    }

    public List<JobPostResponseDto> filterJobPosts(String jobTitle,
                                                   String location,
                                                   String experienceLevel,
                                                   String workType,
                                                   String orderBy,
                                                   String datePosted,
                                                   Pageable pageable) {
        Specification<JobPost> spec = (root, query, cb) -> cb.conjunction();

        if (jobTitle != null && !jobTitle.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + jobTitle.toLowerCase() + "%"));
        }

        if (location != null && !location.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("location")), location.toLowerCase()));
        }
        if (experienceLevel != null && !experienceLevel.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("experienceLevel"), experienceLevel));
        }

        if (workType != null && !workType.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("workType"), workType));
        }
        if (datePosted != null && !datePosted.equalsIgnoreCase("Anytime")) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime threshold = switch (datePosted.toLowerCase()) {
                case "past 24 hours" -> now.minusHours(24);
                case "past 3 days"   -> now.minusDays(3);
                case "past week"     -> now.minusWeeks(1);
                case "past month"    -> now.minusMonths(1);
                default -> null;
            };
            if (threshold != null) {
                spec = spec.and((root, query, cb) ->
                        cb.greaterThanOrEqualTo(root.get("createdAt"), threshold));
            }
        }

        List<JobPost> filteredJobPosts = jobPostRepository.findAll(spec, pageable).getContent();

        return filteredJobPosts.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());

    }
}
