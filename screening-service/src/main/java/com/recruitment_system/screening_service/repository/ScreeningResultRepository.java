package com.recruitment_system.screening_service.repository;

import com.recruitment_system.screening_service.model.ScreeningResult;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ScreeningResultRepository extends MongoRepository<ScreeningResult,Long> {
  List<ScreeningResult> findByJobPostIdOrderByScoreDesc(Long jobPostId);
  Optional<ScreeningResult> findByJobPostIdAndEmail(Long jobPostId, String email);
  ScreeningResult findByApplicationId(Long applicationId);

}
