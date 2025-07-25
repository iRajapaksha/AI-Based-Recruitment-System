package com.ai_based_recruitment_system.repository;

import com.ai_based_recruitment_system.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {
}
