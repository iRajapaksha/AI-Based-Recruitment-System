package com.recruitment_system.organization_service.dto;

import jakarta.persistence.*;
import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {
    private Long id;
    private String name;
    public Skill(String name) {
        this.name = name;
    }
}
