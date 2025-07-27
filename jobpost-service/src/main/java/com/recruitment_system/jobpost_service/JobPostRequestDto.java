package com.recruitment_system.jobpost_service;



public class JobPostRequestDto {
    private String title;
    private String description;
    private String requirements;

    private String status;
    private Long orgId;

    public JobPostRequestDto(){

    }

    public JobPostRequestDto(String title, String description, String requirements, Long orgId,String status) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.orgId = orgId;
        this.status = status;
    }

    public String getStatus() {
        return status;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
