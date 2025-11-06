package com.recruitment_system.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSavedEvent {
    private Long postId;
}
