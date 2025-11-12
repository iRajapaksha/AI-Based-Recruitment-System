package com.recruitment_system.transcript_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TranscriptServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TranscriptServiceApplication.class, args);
	}

}
