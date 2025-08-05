package com.recruitment_system.resume_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ResumeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeServiceApplication.class, args);
	}

}
