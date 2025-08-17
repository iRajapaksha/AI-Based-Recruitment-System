package com.recruitment_system.screening_service.controller;

import com.recruitment_system.screening_service.model.ScreeningResult;
import com.recruitment_system.screening_service.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/screening")
public class ScreeningController {
    private final ScreeningService screeningService;

    @PostMapping("/job-post/{jobPostId}")
    public ResponseEntity<Void> screenJobPost(
            @PathVariable Long jobPostId,
            @RequestBody Map<String, String> body) {
        screeningService.screenJobPost(jobPostId, body.get("jobDescription"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/job-post/{jobPostId}")
    public List<ScreeningResult> getRankedList(@PathVariable Long jobPostId) {
        return screeningService.getRankedResults(jobPostId);
    }
}

