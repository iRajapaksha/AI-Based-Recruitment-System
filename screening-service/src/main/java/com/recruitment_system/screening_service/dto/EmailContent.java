package com.recruitment_system.screening_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailContent {
    private String subject;
    private String body;
}
