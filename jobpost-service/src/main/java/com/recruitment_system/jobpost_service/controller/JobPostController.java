package com.recruitment_system.jobpost_service.controller;

import com.recruitment_system.jobpost_service.dto.ApiResponse;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.dto.JobPostDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RequestMapping("/job-posts")
@RestController
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> create(@Valid @RequestBody JobPostDto request) {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Job post created",
                        jobPostService.createJobPost(request))
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(true,"List of all job posts",
                        jobPostService.getAll())
        );
    }

    @GetMapping("/get/org/{orgId}")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getByOrganization(@PathVariable Long orgId) {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"List of all job posts by organization",
                        jobPostService.getByOrganization(orgId)
        ));

    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> deleteJobPost(@PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Job post deleted.",
                        jobPostService.deletePostById(postId)
                ));

    }
    @DeleteMapping("/delete/org/{orgId}")
    public  ResponseEntity<ApiResponse<List<JobPostResponseDto>>> deletePostByOrgId(@PathVariable Long orgId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Delete all job posts by organization.",
                        jobPostService.deletePostByOrgId(orgId)
                ));

    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> updateJobPost(@PathVariable Integer postId,
                                            @RequestBody Map<String,Object> updates) {
        Long id = Long.valueOf(postId);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Update job post.",
                        jobPostService.updateJobPost(id, updates)
                ));

    }
    @GetMapping("/get/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> getJobPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Retrieved job post by ID.",
                        jobPostService.getJobPostById(postId)

        ));
    }

}
