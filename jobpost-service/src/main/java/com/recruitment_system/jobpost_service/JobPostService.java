package com.recruitment_system.jobpost_service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobPostService {
    private final JobPostRepository jobPostRepository;

    public JobPostService(JobPostRepository jobPostRepository) {
        this.jobPostRepository = jobPostRepository;
    }

    public JobPostResponseDto createJobPost(JobPostRequestDto request) {
        JobPost jobPost =new  JobPost();
        jobPost.setTitle(request.getTitle());
        jobPost.setDescription(request.getDescription());
        jobPost.setRequirements(request.getRequirements());
        jobPost.setCreatedAt(LocalDateTime.now());
        jobPost.setOrgId(request.getOrgId());
        jobPost.setStatus(request.getStatus());

        JobPost saved = jobPostRepository.save(jobPost);

        return mapToResponse(saved);
    }

    public List<JobPostResponseDto> getAll() {
        return jobPostRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<JobPostResponseDto> getByOrganization(Long orgId) {
        return jobPostRepository.findByOrgId(orgId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private JobPostResponseDto mapToResponse(JobPost jobPost) {
        JobPostResponseDto jobPostResponseDto = new JobPostResponseDto();
        jobPostResponseDto.setId(jobPost.getId());
        jobPostResponseDto.setDescription(jobPost.getDescription());
        jobPostResponseDto.setTitle(jobPost.getTitle());
        jobPostResponseDto.setStatus(jobPost.getStatus());
        jobPostResponseDto.setOrgId(jobPost.getOrgId());
        jobPostResponseDto.setRequirements(jobPost.getRequirements());
        jobPostResponseDto.setCreatedAt(jobPost.getCreatedAt());

       return jobPostResponseDto;
    }

    public JobPost deletePostById(Long id) {
        return jobPostRepository.findById(id).map(jobPost -> {
            jobPostRepository.delete(jobPost);
            return jobPost;
        }).orElseThrow(() -> new RuntimeException("Job post not found with ID: " + id));
    }


    @Transactional
    public List<JobPost> deletePostByOrgId(Long orgId) {
        List<JobPost> jobPosts = jobPostRepository.findByOrgId(orgId); // fetch for return
        jobPostRepository.deleteAllByOrgId(orgId); // perform deletion
        return jobPosts;
    }

    public JobPost updateJobPost(Long postId, JobPostRequestDto updatedPost) {
        return jobPostRepository.findById(postId).map(existingPost -> {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setDescription(updatedPost.getDescription());
            existingPost.setRequirements(updatedPost.getRequirements());
            existingPost.setStatus(updatedPost.getStatus());
            existingPost.setOrgId(updatedPost.getOrgId());
            return jobPostRepository.save(existingPost);
        }).orElseThrow(() -> new RuntimeException("Job post not found with ID: " + postId));
    }


}
