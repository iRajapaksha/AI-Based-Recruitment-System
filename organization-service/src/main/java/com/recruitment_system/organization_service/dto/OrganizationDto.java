package com.recruitment_system.organization_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class OrganizationDto {

    @NotBlank(message = "Company email is required")
    @Email(message = "Invalid email format")
    private String companyEmail;

    @NotBlank(message = "Organization name is required")
    @Size(min = 2, max = 100, message = "Organization name must be between 2 and 100 characters")
    private String organizationName;

    // You may want to accept a URL or Base64 string for logo
    @NotBlank(message = "Organization logo URL is required")
    @Pattern(
            regexp = "^(https?|ftp)://.*$",
            message = "Organization logo must be a valid URL"
    )
    private String organizationLogo;

    @NotBlank(message = "Industry is required")
    @Size(max = 50, message = "Industry name should not exceed 50 characters")
    private String industry;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description should not exceed 500 characters")
    private String description;

    @NotBlank(message = "Company size is required")
    @Pattern(
            regexp = "^(1-10|11-50|51-200|201-500|501-1000|1000\\+)$",
            message = "Company size must follow valid ranges like '1-10', '11-50', '1000+'"
    )
    private String companySize;

    @NotBlank(message = "Founded year is required")
    @Pattern(
            regexp = "^(19|20)\\d{2}$",
            message = "Founded year must be a valid 4-digit year (1900–2099)"
    )
    private String foundedYear;

    @NotBlank(message = "Company phone is required")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Company phone must be valid and contain 7–15 digits"
    )
    private String companyPhone;

    @NotBlank(message = "Company location is required")
    @Size(max = 200, message = "Company location should not exceed 200 characters")
    private String companyLocation;

    @NotBlank(message = "Company website is required")
    @Pattern(
            regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9\\-]+\\.[a-z]{2,}.*$",
            message = "Company website must be a valid URL"
    )
    private String companyWebsite;
}
