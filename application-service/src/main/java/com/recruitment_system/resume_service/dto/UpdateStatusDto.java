package com.recruitment_system.resume_service.dto;

import com.recruitment_system.resume_service.model.ApplicationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusDto {
    private ApplicationStatus applicationStatus;

}
