package com.recruitment_system.resume_service.controller;

import com.recruitment_system.resume_service.dto.ApiResponse;
import com.recruitment_system.resume_service.dto.ResumeDto;
import com.recruitment_system.resume_service.dto.ResumeResponseDto;
import com.recruitment_system.resume_service.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> saveResume(@RequestBody ResumeDto dto){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Resume saved.",
                        resumeService.saveResume(dto))
        );
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> getAllByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Get all resumes for post.",
                        resumeService.getByPostId(postId))
        );

    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse<List<ResumeResponseDto>>> deleteAllByPostId(@PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Delete all resumes by post.",
                        resumeService.deleteAllByPostId(postId))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponseDto>> getById(@PathVariable Long id){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Get resume by id.",
                        resumeService.getById(id))
        );

    }

}
