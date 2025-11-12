package com.recruitment_system.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private static final String TEST_EMAIL = "testuser@example.com";

	@BeforeEach
	void setup() {
		userProfileRepository.deleteAll();
	}

	@Test
	@Order(1)
	void createUserProfile_ShouldReturn201() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/users/create")
						.content(TEST_EMAIL)
						.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.success")
						.value(true))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.message")
						.value("User profile created successfully."));
	}

}
