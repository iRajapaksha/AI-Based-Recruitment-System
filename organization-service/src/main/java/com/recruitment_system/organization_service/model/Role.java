package com.recruitment_system.organization_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    APPLICANT, ORG, ADMIN,UNSET;

    @JsonCreator
    public static Role fromString(String value) {
        try {
            return Role.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + value);
        }
    }
}
