package com.ai_based_recruitment_system;

import com.ai_based_recruitment_system.model.Role;
import com.ai_based_recruitment_system.model.UserEntity;
import com.ai_based_recruitment_system.repository.RoleRepository;
import com.ai_based_recruitment_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RecruitmentSystemApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentSystemApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		List<UserEntity> users = userRepository.findAll();
		List<Role> roles = roleRepository.findAll();
		roles.forEach(System.out::println);
		users.forEach(System.out::println);
	}

}
