package com.recruitment_system.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveScreeningResultEvent {
   // private Long applicationId;
    private Long postId;
    private String email;
    private Double score;
}
