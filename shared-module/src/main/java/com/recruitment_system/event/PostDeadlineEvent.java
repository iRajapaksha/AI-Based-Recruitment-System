package com.recruitment_system.event;

public class PostDeadlineEvent {
    private Long jobPostId;

    public PostDeadlineEvent() {
    }

    public PostDeadlineEvent(Long jobId) {
        this.jobPostId = jobId;
    }

    public Long getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Long jobPostId) {
        this.jobPostId = jobPostId;
    }
}
