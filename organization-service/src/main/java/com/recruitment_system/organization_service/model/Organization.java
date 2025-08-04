package com.recruitment_system.organization_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Organization {

    private String email;
    @Id
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
