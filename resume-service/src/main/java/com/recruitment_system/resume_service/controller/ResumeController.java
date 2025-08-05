package com.recruitment_system.resume_service.controller;

import com.recruitment_system.resume_service.dto.ResumeDto;
import com.recruitment_system.resume_service.dto.ResumeResponseDto;
import com.recruitment_system.resume_service.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/save")
    public ResumeResponseDto saveResume(@RequestBody ResumeDto dto){
        return resumeService.saveResume(dto);
    }

    @GetMapping("/post/{postId}")
    public List<ResumeResponseDto> getAllByPostId(@PathVariable Long postId){
        return resumeService.getByPostId(postId);
    }

    @DeleteMapping("/{postId}")
    public List<ResumeResponseDto> deleteAllByPostId(@PathVariable Long postId){
        return resumeService.deleteAllByPostId(postId);
    }

    @GetMapping("/{id}")
    public ResumeResponseDto getById(@PathVariable Long id){
        return resumeService.getById(id);
    }

}
