package com.recruitment_system.resume_service.controller;

import com.recruitment_system.resume_service.dto.ApiResponse;
import com.recruitment_system.resume_service.dto.ApplicationDto;
import com.recruitment_system.resume_service.dto.ApplicationResponseDto;
import com.recruitment_system.resume_service.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllByPostId(@PathVariable Long postId){
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
                new ApiResponse<>(true,"Get resume by id.",
                        applicationService.getById(id))
        );

    }
}
