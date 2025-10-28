package com.recruitment_system.screening_service.repository;

import com.recruitment_system.screening_service.model.EmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRequestRepository extends JpaRepository<EmailRequest, Long> {
}
