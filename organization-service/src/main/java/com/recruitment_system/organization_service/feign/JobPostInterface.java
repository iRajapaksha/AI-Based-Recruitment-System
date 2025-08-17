package com.recruitment_system.organization_service.feign;

import com.recruitment_system.organization_service.dto.ApiResponse;
import com.recruitment_system.organization_service.dto.JobPostDto;
import com.recruitment_system.organization_service.dto.JobPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "JOBPOST-SERVICE")
public interface JobPostInterface {

    @PostMapping("/job-posts/create")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> create(@RequestBody JobPostDto request);

    @GetMapping("/job-posts/get-all")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getAll();

    @GetMapping("/job-posts/get/org/{orgId}")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getByOrganization(@PathVariable Long orgId);

    @DeleteMapping("/job-posts/delete/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> deleteJobPost(@PathVariable Long postId);

    @DeleteMapping("/job-posts/delete/org/{orgId}")
    public  ResponseEntity<ApiResponse<List<JobPostResponseDto>>> deletePostByOrgId(@PathVariable Long orgId);

    @PatchMapping("/job-posts/update/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> updateJobPost(@PathVariable Integer postId,
                                                                         @RequestBody Map<String,Object> updates);

    @GetMapping("/job-posts/get/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> getJobPostById(@PathVariable Long postId);
}