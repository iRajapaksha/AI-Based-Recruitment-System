package com.recruitment_system.jobpost_service;


import java.time.LocalDateTime;

public class JobPostResponseDto {
    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String status;
    private Long orgId;
    private LocalDateTime createdAt;

    public JobPostResponseDto() {

    }

    public JobPostResponseDto(Long id, String title, String description, String status,
                              Long orgId,String requirements,LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.orgId = orgId;
        this.requirements = requirements;
        this.createdAt = createdAt;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getStatus() {
        return status;
    }

    public Long getOrgId() {
        return orgId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
