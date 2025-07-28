package com.recruitment_system.jobpost_service.controller;

import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.dto.JobPostRequestDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.service.JobPostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@PreAuthorize("hasRole('ORG')")
@RequestMapping("/jobposts")
@RestController
public class JobPostController {

    private final JobPostService jobPostService;

    public JobPostController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping("/create")
    public JobPostResponseDto create(@RequestBody JobPostRequestDto request) {
        return jobPostService.createJobPost(request);
    }

    @GetMapping
    public List<JobPostResponseDto> getAll() {
        return jobPostService.getAll();
    }

    @GetMapping("/org/{orgId}")
    public List<JobPostResponseDto> getByOrganization(@PathVariable Long orgId) {
        return jobPostService.getByOrganization(orgId);
    }

    @DeleteMapping("/{postId}")
    public JobPost deleteJobPost(@PathVariable Long postId){
        return jobPostService.deletePostById(postId);
    }
    @DeleteMapping("/org/{orgId}")
    public List<JobPost> deletePostByOrgId(@PathVariable Long orgId){
        return jobPostService.deletePostByOrgId(orgId);
    }

    @PutMapping("/{postId}")
    public JobPost updateJobPost(@PathVariable Long postId, @RequestBody JobPostRequestDto updatedPost) {
        return jobPostService.updateJobPost(postId, updatedPost);
    }

}
