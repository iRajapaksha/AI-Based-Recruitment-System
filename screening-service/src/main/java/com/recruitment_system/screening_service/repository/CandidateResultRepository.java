package com.recruitment_system.screening_service.repository;

import com.recruitment_system.screening_service.model.CandidateResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateResultRepository extends JpaRepository<CandidateResult,Long> {
    List<CandidateResult> findByJobPostIdOrderByRankAsc(Long jobPostId);

}
