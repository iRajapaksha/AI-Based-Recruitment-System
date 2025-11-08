package com.recruitment_system.resume_service.repository;

import com.recruitment_system.resume_service.model.Application;
import com.recruitment_system.resume_service.model.ApplicationStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application,Long> {

    Optional<List<Application>> findByPostId(Long postId);
    Optional<Application> findByUserEmailAndPostId(String userEmail, Long postId);
    List<Application> findByUserEmailAndApplicationStatus(String userEmail, ApplicationStatus applicationStatus);

    @Modifying
    @Transactional
    @Query("DELETE FROM Application r WHERE r.postId = :postId")
    int deleteByPostId(@Param("postId") Long postId);
}
