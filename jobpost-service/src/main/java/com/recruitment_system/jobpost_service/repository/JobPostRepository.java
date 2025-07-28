package com.recruitment_system.jobpost_service.repository;

import com.recruitment_system.jobpost_service.model.JobPost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost,Long> {
    List<JobPost> findByOrgId(Long orgId);
    @Modifying
    @Transactional
    @Query("DELETE FROM JobPost jp WHERE jp.orgId = :orgId")
    int deleteAllByOrgId(@Param("orgId") Long orgId);

}
