package com.recruitment_system.organization_service.dto;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class OrganizationResponseDto {
    private Long orgId;
    private String userEmail;
    private String companyEmail;
    private String organizationName;
    private String organizationLogo;
    private String industry;
    private String description;
    private String companySize;
    private String foundedYear;
    private String companyPhone;
    private String companyLocation;
    private String companyWebsite;
}
