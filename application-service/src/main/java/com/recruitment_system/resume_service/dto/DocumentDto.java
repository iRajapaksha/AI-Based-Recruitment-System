package com.recruitment_system.resume_service.dto;

import com.recruitment_system.resume_service.model.DocumentType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DocumentDto {
    @NotBlank(message = "Document ID is required")
    private String id;

    @NotBlank(message = "Document type is required")
    private DocumentType type;

    @NotBlank(message = "Document URL is required")
    private String url;
}


