package com.recruitment_system.resume_service.repository;

import com.recruitment_system.resume_service.model.Resume;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume,Long> {

    Optional<List<Resume>> findByPostId(Long postId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Resume r WHERE r.postId = :postId")
    int deleteByPostId(@Param("postId") Long postId);
}
