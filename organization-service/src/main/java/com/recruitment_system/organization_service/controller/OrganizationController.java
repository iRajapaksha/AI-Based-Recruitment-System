package com.recruitment_system.organization_service.controller;

import com.recruitment_system.organization_service.dto.ApiResponse;
import com.recruitment_system.organization_service.dto.JobPostResponseDto;
import com.recruitment_system.organization_service.dto.OrganizationDto;
import com.recruitment_system.organization_service.dto.OrganizationResponseDto;
import com.recruitment_system.organization_service.feign.JobPostInterface;
import com.recruitment_system.organization_service.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;
    private final JobPostInterface jobPostInterface;

    @GetMapping("/me/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> getMyOrg(Authentication auth
            , @PathVariable Long orgId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Retrieved organization."
                        ,organizationService.getOrg(orgId))
        );

    }

    @GetMapping("/get-logo/{orgId}")
    public ResponseEntity<String> getLogoUrl(@PathVariable Long orgId){
        return ResponseEntity.ok(
                organizationService.getLogoUrl(orgId)
        );

    }

    @GetMapping("/my-orgs")
    public ResponseEntity<ApiResponse<List<OrganizationResponseDto>>> getMyOrgs(Authentication auth){
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true,"List of organizations."
                        ,organizationService.getMyOrgs(email))
        );

    }

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> createOrg(Authentication auth,
                                                                  @Valid @RequestBody OrganizationDto req){
        String email = auth.getName();
        return  ResponseEntity.ok(
                new ApiResponse<>(true,"Organization created successfully."
                        ,organizationService.createOrg(req,email))
        );
    }

    @PatchMapping("/me/{orgId}")
    public ResponseEntity<ApiResponse<OrganizationResponseDto>> updateOrg(Authentication auth,
                                                                  @RequestBody Map<String,Object> updates,
                                     @PathVariable Long orgId){
        return  ResponseEntity.ok(
                new ApiResponse<>(true,"Organization updated successfully."
                        ,organizationService.updateOrg(orgId,updates))
        );

    }

    @DeleteMapping("/me/{orgId}")
    public  ResponseEntity<ApiResponse<OrganizationResponseDto>> deleteOrg(Authentication auth,
                                                                   @PathVariable Long orgId){
        return  ResponseEntity.ok(
                new ApiResponse<>(true,"Organization deleted successfully.",
                        organizationService.deleteOrg(orgId))
        );
    }

    @GetMapping("/get-my-posts/{orgId}")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getMyPosts(@PathVariable Long orgId){
        ResponseEntity<ApiResponse<List<JobPostResponseDto>>> response = jobPostInterface.getByOrganization(orgId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Retrieved all job posts.",
                        response.getBody().getData())
        );
    }

}
