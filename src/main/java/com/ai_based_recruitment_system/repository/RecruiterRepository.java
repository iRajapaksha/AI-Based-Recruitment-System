package com.ai_based_recruitment_system.repository;


import com.ai_based_recruitment_system.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {
}
