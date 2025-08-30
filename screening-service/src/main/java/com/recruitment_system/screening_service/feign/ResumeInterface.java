package com.recruitment_system.screening_service.feign;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.screening_service.dto.ResumeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "RESUME-SERVICE")
public interface ResumeInterface {
    @GetMapping("/resumes/post/{postId}")
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> getAllByPostId(@PathVariable Long postId);

    @DeleteMapping("/resumes/{postId}")
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> deleteAllByPostId(@PathVariable Long postId);
}
