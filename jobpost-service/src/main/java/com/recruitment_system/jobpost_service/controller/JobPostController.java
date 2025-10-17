package com.recruitment_system.jobpost_service.controller;

import com.recruitment_system.jobpost_service.dto.ApiResponse;
import com.recruitment_system.jobpost_service.dto.JobPostUpdateDto;
import com.recruitment_system.jobpost_service.dto.JobPostCreateDto;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/job-posts")
@RestController
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> create(
            @Valid @RequestBody JobPostCreateDto request,
            Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Job post created",
                        jobPostService.createJobPost(request,email))
        );
    }

    @PostMapping("/drafts")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> saveDraft(
            @RequestBody JobPostUpdateDto draft,
            Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job post draft saved",
                        jobPostService.saveDraft(draft,email))
        );
    }

    @GetMapping("/drafts")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> getDraft(
            Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job post draft retrieved",
                        jobPostService.getDraft(email))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getMyJobPosts(
            Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "List of my job posts",
                        jobPostService.getMyJobPosts(email))
        );
    }

    @PatchMapping("/drafts/{id}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> updateDraft(
            @PathVariable Long id,
            @RequestBody Map<String,Object> updates) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job post draft updated",
                        jobPostService.updateDraft(id, updates))
        );
    }

    @PutMapping("/publish/{id}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> publishDraft(
            @PathVariable Long id,
            @Valid @RequestBody JobPostCreateDto request) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Job post published",
                        jobPostService.publishDraft(id, request))
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getAll() {

        return ResponseEntity.ok(
                new ApiResponse<>(true,"List of all job posts",
                        jobPostService.getAll())
        );
    }

    @GetMapping("/get/filter")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> filterJobPosts(
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) String workType,
            @RequestParam(required = false, defaultValue = "id") String orderBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String datePosted,
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        Sort sort = null;
        if(sortDirection.equalsIgnoreCase("ASC")){
            sort = Sort.by(orderBy).ascending();
        } else{
            sort = Sort.by(orderBy).descending();
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);
        List<JobPostResponseDto> filteredPosts = jobPostService.filterJobPosts(jobTitle,
                location, experienceLevel, workType, orderBy, datePosted,pageable);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Filtered job posts", filteredPosts)
        );
    }

    @GetMapping("/get/org/{orgId}")
    public ResponseEntity<ApiResponse<List<JobPostResponseDto>>> getByOrganization(@PathVariable Long orgId) {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"List of all job posts by organization",
                        jobPostService.getByOrganization(orgId)
        ));

    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> deleteJobPost(@PathVariable Long postId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Job post deleted.",
                        jobPostService.deletePostById(postId)
                ));

    }
    @DeleteMapping("/delete/org/{orgId}")
    public  ResponseEntity<ApiResponse<List<JobPostResponseDto>>> deletePostByOrgId(@PathVariable Long orgId){
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Delete all job posts by organization.",
                        jobPostService.deletePostByOrgId(orgId)
                ));

    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> updateJobPost(
            @PathVariable Long postId,
            @RequestBody JobPostUpdateDto updateDto) {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Update job post.",
                        jobPostService.updateJobPost(postId, updateDto)
                ));

    }
    @GetMapping("/get/{postId}")
    public ResponseEntity<ApiResponse<JobPostResponseDto>> getJobPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Retrieved job post by ID.",
                        jobPostService.getJobPostById(postId)

        ));
    }

}
