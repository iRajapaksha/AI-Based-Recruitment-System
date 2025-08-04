package com.recruitment_system.jobpost_service.controller;

import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.dto.JobPostDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RequestMapping("/job-posts")
@RestController
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;


    @PostMapping("/create")
    public JobPostResponseDto create(@RequestBody JobPostDto request) {
        return jobPostService.createJobPost(request);
    }

    @GetMapping("/get-all")
    public List<JobPostResponseDto> getAll() {
        return jobPostService.getAll();
    }

    @GetMapping("/org/{orgId}")
    public List<JobPostResponseDto> getByOrganization(@PathVariable Long orgId) {
        return jobPostService.getByOrganization(orgId);
    }

    @DeleteMapping("/{postId}")
    public JobPostResponseDto deleteJobPost(@PathVariable Long postId){

        return jobPostService.deletePostById(postId);
    }
    @DeleteMapping("/org/{orgId}")
    public List<JobPostResponseDto> deletePostByOrgId(@PathVariable Long orgId){

        return jobPostService.deletePostByOrgId(orgId);
    }

    @PatchMapping("/{postId}")
    public JobPostResponseDto updateJobPost(@PathVariable Integer postId,
                                            @RequestBody Map<String,Object> updates) {
        Long id = Long.valueOf(postId);
        return jobPostService.updateJobPost(id, updates);
    }

}
