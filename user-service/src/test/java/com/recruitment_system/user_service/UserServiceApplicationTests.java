package com.recruitment_system.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment_system.user_service.dto.UserProfileDto;
import com.recruitment_system.user_service.model.UserProfile;
import com.recruitment_system.user_service.repository.UserProfileRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
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

	@Test
	@Order(2)
	@WithMockUser(username = TEST_EMAIL)
	void getMyProfile_ShouldReturnProfile() throws Exception {
		// Pre-insert profile
		userProfileRepository.save(UserProfile.builder()
				.email(TEST_EMAIL)
				.firstname("John")
				.lastname("Doe")
				.build());

		mockMvc.perform(MockMvcRequestBuilders.get("/users/me"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.data.email")
						.value(TEST_EMAIL))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.data.firstname")
						.value("John"));
	}
	@Test
	@Order(3)
	@WithMockUser(username = TEST_EMAIL)
	void updateMyProfile_ShouldUpdateFields() throws Exception {
		userProfileRepository.save(UserProfile.builder()
				.email(TEST_EMAIL)
				.firstname("John")
				.lastname("Doe")
				.build());

		UserProfileDto updateDto = UserProfileDto.builder()
				.firstname("UpdatedName")
				.location("Colombo")
				.build();

		mockMvc.perform(MockMvcRequestBuilders.patch("/users/me")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.data.firstname")
						.value("UpdatedName"))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.data.location")
						.value("Colombo"));
	}

	@Test
	@Order(4)
	void getUserByEmail_ShouldReturnProfile() throws Exception {
		userProfileRepository.save(UserProfile.builder()
				.email(TEST_EMAIL)
				.firstname("Jane")
				.build());

		mockMvc.perform(MockMvcRequestBuilders.get("/users/" + TEST_EMAIL))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.
						jsonPath("$.email").
						value(TEST_EMAIL))
				.andExpect(MockMvcResultMatchers
						.jsonPath("$.firstname")
						.value("Jane"));
	}

	@Test
	@Order(5)
	void getAllUsers_ShouldReturnList() throws Exception {
		userProfileRepository.save(UserProfile.builder()
				.email("user1@example.com").firstname("User1").build());
		userProfileRepository.save(UserProfile.builder()
				.email("user2@example.com").firstname("User2").build());

		mockMvc.perform(MockMvcRequestBuilders.get("/users/all"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].email").exists());
	}


}
