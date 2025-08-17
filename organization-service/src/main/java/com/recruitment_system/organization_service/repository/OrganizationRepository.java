package com.recruitment_system.organization_service.repository;

import com.recruitment_system.organization_service.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    Optional<Organization> findOrganizationByCompanyEmail(String companyEmail);
    Optional<List<Organization>> findOrganizationByEmail(String email);

}
