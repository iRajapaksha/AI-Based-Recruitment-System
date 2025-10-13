package com.recruitment_system.jobpost_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORGANIZATION-SERVICE")
public interface OrganizationInterface {

    @GetMapping("/organizations/get-logo/{orgId}")
    public ResponseEntity<String> getLogo(@PathVariable Long orgId);
}
