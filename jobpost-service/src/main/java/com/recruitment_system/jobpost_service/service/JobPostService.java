package com.recruitment_system.jobpost_service.service;

import com.recruitment_system.jobpost_service.dto.JobPostUpdateDto;
import com.recruitment_system.jobpost_service.dto.JobPostCreateDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.feign.OrganizationInterface;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.model.Skill;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import com.recruitment_system.jobpost_service.repository.SkillRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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
    private final OrganizationInterface organizationInterface;
    public JobPostResponseDto createJobPost(JobPostCreateDto post, String email) {
        List<Skill> skillEntities = post.getSkills().stream()
                .map(skill -> skillRepository.findByName(skill.getName())
                        .orElseGet(() -> new Skill(skill.getName())))
                .collect(Collectors.toList());

        ResponseEntity<String> response = organizationInterface.getLogo(post.getOrgId());

        if (response.getBody() == null) {
            throw new RuntimeException("Organization not found with ID: " + post.getOrgId());
        }

        String logo = response.getBody();


        JobPost jobPost = JobPost.builder()
                .companyName(post.getCompanyName())
                .companyLogo(logo)
                .deadline(post.getDeadline())
                .title(post.getTitle())
                .requirements(post.getRequirements())
                .benefits(post.getBenefits())
                .employmentType(post.getEmploymentType())
                .experienceLevel(post.getExperienceLevel())
                .minSalary(post.getMinSalary())
                .maxSalary(post.getMaxSalary())
                .currency(post.getCurrency())
                .createdAt(LocalDateTime.now())
                .createdBy(email)
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

    public JobPostResponseDto saveDraft(JobPostUpdateDto draft, String email) {
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
                .benefits(draft.getBenefits())
                .currency(draft.getCurrency())
                .deadline(draft.getDeadline())
                .orgId(draft.getOrgId())
                .createdAt(LocalDateTime.now())
                .createdBy(email)
                .isActive(false)
                .isDraft(true)
                .skills(draft.getSkills() != null ? draft.getSkills() : new ArrayList<Skill>())
                .build();

        JobPost saved = jobPostRepository.save(jobPost);
        return mapToResponseDto(saved);
    }

    public JobPostResponseDto publishDraft(Long draftId, JobPostCreateDto completePost) {
        JobPost existing = jobPostRepository.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found"));
        ResponseEntity<String> response = organizationInterface.getLogo(completePost.getOrgId());

        if (response.getBody() == null) {
            throw new RuntimeException("Organization not found with ID: " + completePost.getOrgId());
        }

        String logo = response.getBody();

        existing.setCompanyName(completePost.getCompanyName());
        existing.setCompanyLogo(logo);
        existing.setLocation(completePost.getLocation());
        existing.setWorkType(completePost.getWorkType());
        existing.setExperienceLevel(completePost.getExperienceLevel());
        existing.setEmploymentType(completePost.getEmploymentType());
        existing.setMinSalary(completePost.getMinSalary());
        existing.setMaxSalary(completePost.getMaxSalary());
        existing.setTitle(completePost.getTitle());
        existing.setDescription(completePost.getDescription());
        existing.setRequirements(completePost.getRequirements());
        existing.setBenefits(completePost.getBenefits());
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

    public JobPostResponseDto getDraft(String email){
        JobPost draft = jobPostRepository.findByCreatedByAndIsDraftTrue(email)
                .orElseThrow(()->new RuntimeException("No draft found for user: " + email));
        return mapToResponseDto(draft);
    }

    public List<JobPostResponseDto> getAll() {
        return jobPostRepository.findByIsActiveTrue()
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

    public JobPostResponseDto updateJobPost(Long postId, JobPostUpdateDto dto) {
        JobPost post = jobPostRepository.findById(postId)
                .orElseThrow(()-> new RuntimeException("Job post not found"));
        if(dto.getCompanyName() != null) post.setCompanyName(dto.getCompanyName());
        if(dto.getCompanyLogo() != null) post.setCompanyLogo(dto.getCompanyLogo());
        if(dto.getLocation() != null) post.setLocation(dto.getLocation());
        if(dto.getWorkType() != null) post.setWorkType(dto.getWorkType());
        if(dto.getExperienceLevel() != null) post.setExperienceLevel(dto.getExperienceLevel());
        if(dto.getEmploymentType() != null) post.setEmploymentType(dto.getEmploymentType());
        if(dto.getMinSalary() != null) post.setMinSalary(dto.getMinSalary());
        if(dto.getMaxSalary() != null) post.setMaxSalary(dto.getMaxSalary());
        if(dto.getTitle() != null) post.setTitle(dto.getTitle());
        if(dto.getDescription() != null) post.setDescription(dto.getDescription());
        if(dto.getRequirements() != null) post.setRequirements(dto.getRequirements());
        if(dto.getBenefits() != null) post.setBenefits(dto.getBenefits());
        if(dto.getDeadline() != null) post.setDeadline(dto.getDeadline());
        if(dto.getSkills() != null) post.setSkills(dto.getSkills());
        if(dto.getOrgId() != null) post.setOrgId(dto.getOrgId());
        if(dto.getCurrency() != null) post.setCurrency(dto.getCurrency());
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
                .companyLogo(post.getCompanyLogo())
                .deadline(post.getDeadline())
                .title(post.getTitle())
                .requirements(post.getRequirements())
                .benefits(post.getBenefits())
                .employmentType(post.getEmploymentType())
                .experienceLevel(post.getExperienceLevel())
                .minSalary(post.getMinSalary())
                .maxSalary(post.getMaxSalary())
                .currency(post.getCurrency())
                .createdAt(post.getCreatedAt())
                .createdBy(post.getCreatedBy())
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

    public JobPostResponseDto getDraftByPostId(Long id) {
        JobPost post = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found with ID: " + id));
        if(!post.getIsDraft()){
            throw new RuntimeException("Job post with ID: " + id + " is not a draft");
        }
        return mapToResponseDto(post);
    }

    public JobPostResponseDto updateDraft(Long draftId, Map<String, Object> updates) {
        JobPost draft = jobPostRepository.findById(draftId)
                .orElseThrow(() -> new RuntimeException("Draft not found"));

        if (!draft.getIsDraft()) {
            throw new RuntimeException("Job post with ID: " + draftId + " is not a draft");
        }

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(JobPost.class, key);
            if (field != null) {
                field.setAccessible(true);

                // Convert LocalDateTime fields manually if value is a String
                if (field.getType().equals(LocalDateTime.class) && value instanceof String) {
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse((String) value);
                        ReflectionUtils.setField(field, draft, dateTime);
                    } catch (Exception e) {
                        // Try another format (e.g. "yyyy-MM-dd")
                        try {
                            LocalDateTime dateTime = LocalDateTime.parse((String) value).toLocalDate().atStartOfDay();
                            ReflectionUtils.setField(field, draft, dateTime);
                        } catch (Exception ex) {
                            throw new RuntimeException("Invalid date format for field: " + key);
                        }
                    }
                } else {
                    ReflectionUtils.setField(field, draft, value);
                }
            }
        });

        jobPostRepository.save(draft);
        return mapToResponseDto(draft);
    }

    public List<JobPostResponseDto> getMyJobPosts(String email) {
        List<JobPost> posts = jobPostRepository.findByCreatedByAndIsActiveTrue(email)
                .orElseThrow(()->new RuntimeException("No active job posts found for user: " + email));
        return posts.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }
}
