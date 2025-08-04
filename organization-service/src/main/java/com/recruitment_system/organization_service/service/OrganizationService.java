package com.recruitment_system.organization_service.service;

import com.recruitment_system.organization_service.dto.OrganizationDto;
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

    public OrganizationDto createOrg(OrganizationDto req,String email){
        if(organizationRepository.findOrganizationByCompanyEmail(req.getCompanyEmail()).isPresent()){
            throw new RuntimeException("Organization already exists with this email");
        }
        Organization newOrg = Organization.builder()
                .email(email)
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
    public OrganizationDto updateOrg(String email, Map<String, Object>updates){
            Organization org = organizationRepository.findOrganizationByCompanyEmail(email)
                    .orElseThrow(()->new RuntimeException("Organization not found"));
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Organization.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, org, value);
            }
        });
        organizationRepository.save(org);
        return mapToDto(org);

    }
    public OrganizationDto getOrg(String email){
        Organization org = organizationRepository.findOrganizationByCompanyEmail(email)
                .orElseThrow(()->new RuntimeException("Organization not found"));
        return mapToDto(org);
    }
    public List<OrganizationDto> getMyOrgs(String email){
        List<Organization> orgs = organizationRepository.findOrganizationByEmail(email)
                        .orElseThrow(()->new RuntimeException("No organizations found"));
        List<OrganizationDto> listOrgs = new ArrayList<>();
        orgs.forEach((org)->{
           listOrgs.add(mapToDto(org));
        });
        return listOrgs;
    }

    public OrganizationDto deleteOrg(String email){
        Organization org = organizationRepository.findOrganizationByCompanyEmail(email)
                .orElseThrow(()->new RuntimeException("Organization not found"));
        organizationRepository.delete(org);
        return mapToDto(org);
    }

    private OrganizationDto mapToDto(Organization req){
        return OrganizationDto.builder()

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
