package com.recruitment_system.resume_service.repository;

import com.recruitment_system.resume_service.model.ApplicationDocument;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ApplicationDocument d WHERE d.application.id IN :appIds")
    void deleteByApplicationIdIn(@Param("appIds") List<Long> appIds);
}
