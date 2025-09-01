package com.recruitment_system.screening_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ScreeningApiResponseDto {
    private List<CandidateResultDto> results;
    private Boolean success;
}
