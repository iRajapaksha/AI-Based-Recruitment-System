package com.recruitment_system.screening_service.repository;

import com.recruitment_system.screening_service.model.ScreeningResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningResultRepository extends JpaRepository<ScreeningResult,Long> {
//    List<CandidateResult> findByJobPostIdOrderByRankAsc(Long jobPostId);

}
