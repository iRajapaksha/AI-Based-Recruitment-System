package com.recruitment_system.transcript_service.repository;

import com.recruitment_system.transcript_service.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranscriptRepository extends JpaRepository<Transcript,Long> {
}
