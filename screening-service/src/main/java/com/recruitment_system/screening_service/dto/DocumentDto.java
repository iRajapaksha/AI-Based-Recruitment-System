package com.recruitment_system.screening_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentDto {
    private String id;
    private String type;
    private String url;
}
