package com.recruitment_system.jobpost_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobPostServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(JobPostServiceApplication.class, args);
	}
}
