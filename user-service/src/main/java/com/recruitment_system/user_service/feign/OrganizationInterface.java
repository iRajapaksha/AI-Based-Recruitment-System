package com.recruitment_system.user_service.feign;

import com.recruitment_system.user_service.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ORGANIZATION-SERVICE")
public interface OrganizationInterface {

    @GetMapping("/")
    public OrganizationDto getMyOrg(Authentication auth);

    @PostMapping("/")
    public OrganizationDto createOrg(@RequestBody OrganizationDto req);

    @PutMapping("/")
    public OrganizationDto updateOrg(Authentication auth, Map<String,Object> updates);

    @DeleteMapping("/")
    public OrganizationDto deleteOrg(Authentication auth);

}
