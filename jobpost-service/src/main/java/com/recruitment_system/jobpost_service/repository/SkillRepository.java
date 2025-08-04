package com.recruitment_system.jobpost_service.repository;

import com.recruitment_system.jobpost_service.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill,Long> {
    Optional<Skill> findByName(String name);
}
