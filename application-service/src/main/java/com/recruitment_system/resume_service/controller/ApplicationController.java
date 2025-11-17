package com.recruitment_system.resume_service.controller;

import com.recruitment_system.resume_service.dto.ApiResponse;
import com.recruitment_system.resume_service.dto.ApplicationDto;
import com.recruitment_system.resume_service.dto.ApplicationResponseDto;
import com.recruitment_system.resume_service.dto.UpdateStatusDto;
import com.recruitment_system.resume_service.model.ApplicationStatus;
import com.recruitment_system.resume_service.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> saveApplication(
            @Valid @RequestBody ApplicationDto dto){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Application saved.",
                        applicationService.saveApplication(dto))
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllByPostId(
            @PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Get all applications for post.",
                        applicationService.getByPostId(postId))
        );

    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> deleteAllByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Delete all applications by post.",
                        applicationService.deleteAllByPostId(postId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> getById(@PathVariable Long id){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Get application by id.",
                        applicationService.getById(id))
        );

    }

    @GetMapping("/interviews")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllInterviews(Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Get all applications with interviews.",
                        applicationService.getAllInterviews(email))
        );

    }

    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> setApplicationStatus(
            @PathVariable Long applicationId,
            @RequestBody UpdateStatusDto updateStatusDto){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Application status updated",
                        applicationService.setApplicationStatus(applicationId,updateStatusDto))
        );
    }

    @PatchMapping("/set-interview-score/{applicationId}")
    public ResponseEntity<ApiResponse<ApplicationResponseDto>> setInterviewScore(
            @PathVariable Long applicationId,
            @RequestBody double interviewScore){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Application interview score updated",
                        applicationService.setInterviewScore(applicationId,interviewScore))
        );
    }

}
