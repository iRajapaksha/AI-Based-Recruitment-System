package com.recruitment_system.screening_service.feign;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.dto.JobPostResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOBPOST-SERVICE")
public interface JobPostInterface {
    @GetMapping("/job-posts/get/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> getJobPostById(@PathVariable Long postId);
    @DeleteMapping("/job-posts/delete/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> deleteJobPost(@PathVariable Long postId);
}
