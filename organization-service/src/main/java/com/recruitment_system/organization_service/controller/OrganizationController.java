package com.recruitment_system.organization_service.controller;

import com.recruitment_system.organization_service.dto.OrganizationDto;
import com.recruitment_system.organization_service.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping("/me/{companyEmail}")
    public OrganizationDto getMyOrg(Authentication auth,@PathVariable String companyEmail){
        if(auth ==null || !auth.isAuthenticated()){
            throw new SecurityException("User not authenticated");
        }
        return organizationService.getOrg(companyEmail);

    }

    @GetMapping("/my-orgs")
    public List<OrganizationDto> getMyOrgs(Authentication auth){
        if(auth ==null || !auth.isAuthenticated()){
            throw new SecurityException("User not authenticated");
        }
        String email = auth.getName();
        return organizationService.getMyOrgs(email);

    }

    @PostMapping("/me")
    public OrganizationDto createOrg(Authentication auth,@RequestBody OrganizationDto req){
        if(auth ==null || !auth.isAuthenticated()){
            throw new SecurityException("User not authenticated");
        }
        String email = auth.getName();
        return organizationService.createOrg(req,email);
    }

    @PatchMapping("/me/{companyEmail}")
    public OrganizationDto updateOrg(Authentication auth,@RequestBody Map<String,Object> updates,
                                     @PathVariable String companyEmail){
        if(auth == null || !auth.isAuthenticated()){
            throw new SecurityException("User not authenticated");
        }
        return organizationService.updateOrg(companyEmail,updates);
    }

    @DeleteMapping("/me/{companyEmail}")
    public OrganizationDto deleteOrg(Authentication auth, @PathVariable String companyEmail){
        if (auth == null || !auth.isAuthenticated()){
            throw new SecurityException("User not authenticated");
        }

        return organizationService.deleteOrg(companyEmail);

    }



}
