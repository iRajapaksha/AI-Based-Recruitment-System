package com.recruitment_system.transcript_service.service;

import com.recruitment_system.transcript_service.dto.TranscriptDto;
import com.recruitment_system.transcript_service.dto.TranscriptResponseDto;
import com.recruitment_system.transcript_service.model.Transcript;
import com.recruitment_system.transcript_service.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranscriptService {
    private final TranscriptRepository transcriptRepository;

    public TranscriptResponseDto saveTranscript(TranscriptDto transcriptDto) {
        Transcript transcript =Transcript.builder()
                .transcriptId(transcriptDto.getTranscriptId())
                .applicationId(transcriptDto.getApplicationId())
                .userEmail(transcriptDto.getUserEmail())
                .build();
        transcriptRepository.save(transcript);
        return mapToDto(transcript);

    }

    public TranscriptResponseDto getTranscriptByApplicationId(Long applicationId) {
        Transcript transcript = transcriptRepository.findAll()
                .stream()
                .filter(t -> t.getApplicationId().equals(applicationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Transcript not found for applicationId: " + applicationId));
        return mapToDto(transcript);
    }

    public TranscriptResponseDto mapToDto(Transcript transcript){
        return new TranscriptResponseDto(
                transcript.getApplicationId(),
                transcript.getTranscriptId(),
                transcript.getUserEmail()
        );
    }
}
