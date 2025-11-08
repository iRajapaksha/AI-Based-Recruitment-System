package com.recruitment_system.screening_service.controller;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.screening_service.dto.ConfirmApplicantsDto;
import com.recruitment_system.screening_service.dto.ScreeningResultDto;
import com.recruitment_system.screening_service.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/screening")
public class ScreeningController {
    private final ScreeningService screeningService;

//    @PostMapping("/job-post/{jobPostId}")
//    public ResponseEntity<Void> screenJobPost(
//            @PathVariable Long jobPostId,
//            @RequestBody Map<String, String> body) {
//        screeningService.screenJobPost(jobPostId, body.get("jobDescription"));
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/job-post/{jobPostId}")
    public ResponseEntity<ApiResponse<List<ScreeningResultDto>>> getRankedList(
            @PathVariable Long jobPostId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Ranked list fetched successfully",
                screeningService.getRankedList(jobPostId)));
    }

    @PostMapping("/run/{postId}")
    public ResponseEntity<ApiResponse<List<ScreeningResultDto>>> runScreeningManually(
            @PathVariable Long postId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Screening run successfully",
                screeningService.runScreeningManually(postId)));
    }

    @PostMapping("/job-post/{jobPostId}/confirm")
    public ResponseEntity<ApiResponse<Void>> confirmApplicants(
            @PathVariable Long jobPostId,
            @Valid @RequestBody ConfirmApplicantsDto confirmApplicantsDto) {
        log.info("Received confirm request: {}", confirmApplicantsDto);
        screeningService.confirmApplicants(jobPostId,confirmApplicantsDto);
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Applicants confirmation email sent successfully",
                null));
    }
}

