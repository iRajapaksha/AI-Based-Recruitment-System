package com.recruitment_system.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInterviewDateEvent {
    private Long applicationId;
    private LocalDateTime interviewDate;
}
