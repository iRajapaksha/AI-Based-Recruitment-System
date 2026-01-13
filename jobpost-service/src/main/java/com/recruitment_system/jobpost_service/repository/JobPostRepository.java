package com.recruitment_system.jobpost_service.repository;

import com.recruitment_system.jobpost_service.model.JobPost;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobPostRepository extends JpaRepository<JobPost,Long>, JpaSpecificationExecutor<JobPost> {

    Page<JobPost> findAll(Specification<JobPost> spec, Pageable pageable);

    List<JobPost> findByOrgId(Long orgId);
    List<JobPost> findByIsActiveTrue();
    @Modifying
    @Transactional
    @Query("DELETE FROM JobPost jp WHERE jp.orgId = :orgId")
    int deleteAllByOrgId(@Param("orgId") Long orgId);

    List<JobPost> findByDeadlineBeforeAndIsActive(LocalDateTime now, Boolean isActive);


    Optional<JobPost> findByCreatedByAndIsDraftTrue(String email);

    Optional<List<JobPost>> findByCreatedByAndIsActiveTrue(String email);
    List<JobPost> findByCreatedBy(String email);

}


