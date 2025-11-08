package com.recruitment_system.screening_service.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmApplicantsDto {
    @NotNull(message = "Selected application emails are required")
    private List<String> selectedApplicationEmails;
    @NotNull(message = "Interview date is required")
    @FutureOrPresent
    private LocalDateTime interviewDate;

}
