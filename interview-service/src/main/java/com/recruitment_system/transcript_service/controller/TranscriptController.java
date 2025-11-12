package com.recruitment_system.transcript_service.controller;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.transcript_service.dto.TranscriptDto;
import com.recruitment_system.transcript_service.dto.TranscriptResponseDto;
import com.recruitment_system.transcript_service.service.TranscriptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transcripts")
@RequiredArgsConstructor
public class TranscriptController {
    private final TranscriptService transcriptService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TranscriptResponseDto>> saveTranscript(
            @Valid @RequestBody TranscriptDto transcriptDto
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Transcript saved successfully",
                transcriptService.saveTranscript(transcriptDto)
                ));
    }

    @GetMapping("/get/{applicationId}")
    public ResponseEntity<ApiResponse<TranscriptResponseDto>> getTranscriptByApplicationId(
            @PathVariable Long applicationId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Transcript fetched successfully",
                transcriptService.getTranscriptByApplicationId(applicationId)
        ));
    }
}
