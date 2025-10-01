package com.recruitment_system.organization_service.service;

import com.recruitment_system.organization_service.dto.OrganizationDto;
import com.recruitment_system.organization_service.dto.OrganizationResponseDto;
import com.recruitment_system.organization_service.model.Organization;
import com.recruitment_system.organization_service.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service @RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public OrganizationResponseDto createOrg(OrganizationDto req,String email){
        if(organizationRepository.findOrganizationByCompanyEmail(req.getCompanyEmail()).isPresent()){
            throw new RuntimeException("Organization already exists with this email");
        }
        Organization newOrg = Organization.builder()
                .userEmail(email)
                .companyEmail(req.getCompanyEmail())
                .companyLocation(req.getCompanyLocation())
                .companyPhone(req.getCompanyPhone())
                .companySize(req.getCompanySize())
                .companyWebsite(req.getCompanyWebsite())
                .description(req.getDescription())
                .foundedYear(req.getFoundedYear())
                .organizationLogo(req.getOrganizationLogo())
                .industry(req.getIndustry())
                .organizationName(req.getOrganizationName())
                .build();

        organizationRepository.save(newOrg);
        return mapToDto(newOrg);

    }
    public OrganizationResponseDto updateOrg(Long orgId, Map<String, Object> updates) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(()->new RuntimeException("Organization not found"));

            updates.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Organization.class, key);
                if (field != null) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, org, value);
                }
            });

            Organization updatedOrg = organizationRepository.save(org);
            return mapToDto(updatedOrg);

    }

    public OrganizationResponseDto getOrg(Long orgId){
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(()->new RuntimeException("Organization not found"));
        return mapToDto(org);
    }

    public List<OrganizationResponseDto> getMyOrgs(String email){
        List<Organization> orgs = organizationRepository.findOrganizationByUserEmail(email)
                        .orElseThrow(()->new RuntimeException("No organizations found"));
        List<OrganizationResponseDto> listOrgs = new ArrayList<>();
        orgs.forEach((org)->{
           listOrgs.add(mapToDto(org));
        });
        return listOrgs;
    }

    public OrganizationResponseDto deleteOrg(Long orgId){
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(()->new RuntimeException("Organization not found"));
        organizationRepository.delete(org);
        return mapToDto(org);
    }

    private OrganizationResponseDto mapToDto(Organization req){
        return OrganizationResponseDto.builder()
                .orgId(req.getId())
                .userEmail(req.getUserEmail())
                .companyEmail(req.getCompanyEmail())
                .companyLocation(req.getCompanyLocation())
                .companyPhone(req.getCompanyPhone())
                .companySize(req.getCompanySize())
                .companyWebsite(req.getCompanyWebsite())
                .description(req.getDescription())
                .foundedYear(req.getFoundedYear())
                .organizationLogo(req.getOrganizationLogo())
                .industry(req.getIndustry())
                .organizationName(req.getOrganizationName())
                .build();
    }
}
