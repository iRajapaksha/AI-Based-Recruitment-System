package com.recruitment_system.screening_service.feign;

import com.recruitment_system.dto.ApiResponse;
import com.recruitment_system.screening_service.dto.ApplicationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "APPLICATION-SERVICE")
public interface ApplicationInterface {
    @GetMapping("/applications/post/{postId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> getAllByPostId(@PathVariable Long postId);

    @DeleteMapping("/applications/{postId}")
    public ResponseEntity<ApiResponse<List<ApplicationResponseDto>>> deleteAllByPostId(@PathVariable Long postId);
}
