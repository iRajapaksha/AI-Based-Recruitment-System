package com.recruitment_system.event;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmationEmailEvent implements Serializable {
    private Long applicationId;
    private Long jobPostId;
    private String email;
    private String subject;
    private String body;
//    private String correlationId; // UUID for tracing
//    private Map<String, String> metadata; // optional
}
