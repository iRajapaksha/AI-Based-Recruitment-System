package com.recruitment_system.auth_service.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @PostMapping("/users/create")
    public ResponseEntity<String> createProfile(@RequestBody String email);
}
