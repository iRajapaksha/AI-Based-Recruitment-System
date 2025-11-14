package com.recruitment_system.organization_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recruitment_system.organization_service.dto.ApiResponse;
import com.recruitment_system.organization_service.dto.JobPostResponseDto;
import com.recruitment_system.organization_service.dto.OrganizationDto;
import com.recruitment_system.organization_service.feign.JobPostInterface;
import com.recruitment_system.organization_service.model.Organization;
import com.recruitment_system.organization_service.repository.OrganizationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrganizationServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrganizationRepository organizationRepository;

	@MockitoBean
	private JobPostInterface jobPostInterface; // Mock Feign client

	private Organization org;

	@BeforeAll
	void setup() {
		// Clean database before tests
		organizationRepository.deleteAll();

		// Insert a sample organization
		org = Organization.builder()
				.userEmail("testuser@example.com")
				.companyEmail("org@example.com")
				.organizationName("Test Org")
				.organizationLogo("https://logo.com/logo.png")
				.industry("Tech")
				.description("This is a test organization")
				.companySize("50-100")
				.foundedYear("2020")
				.companyPhone("1234567890")
				.companyLocation("Colombo")
				.companyWebsite("https://testorg.com")
				.build();
		org = organizationRepository.save(org);
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testGetMyOrg() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/organizations/me/{orgId}", org.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.organizationName").value("Test Org"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.companyEmail").value("org@example.com"));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testGetLogoUrl() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/organizations/get-logo/{orgId}", org.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("https://logo.com/logo.png"));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testGetMyOrgs() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/organizations/my-orgs"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].organizationName").value("Test Org"));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testCreateOrg() throws Exception {
		OrganizationDto newOrgDto = OrganizationDto.builder()
				.organizationName("New Org")
				.companyEmail("neworg@example.com")
				.companyLocation("Kandy")
				.companyPhone("0987654321")
				.companySize("11-50")
				.companyWebsite("https://neworg.com")
				.description("New organization")
				.foundedYear("2023")
				.organizationLogo("https://logo.com/newlogo.png")
				.industry("Finance")
				.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/organizations/me")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(newOrgDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.organizationName").value("New Org"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.companyEmail").value("neworg@example.com"));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testUpdateOrg() throws Exception {
		Map<String, Object> updates = Map.of("organizationName", "Updated Org");

		mockMvc.perform(MockMvcRequestBuilders.patch("/organizations/me/{orgId}", org.getId())
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updates)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.organizationName").value("Updated Org"));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testDeleteOrg() throws Exception {
		Organization orgToDelete = Organization.builder()
				.userEmail("deleteuser@example.com")
				.companyEmail("delete@example.com")
				.organizationName("Delete Org")
				.build();
		orgToDelete = organizationRepository.save(orgToDelete);

		mockMvc.perform(MockMvcRequestBuilders.delete("/organizations/me/{orgId}", orgToDelete.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.organizationName").value("Delete Org"));

		Assertions.assertFalse(organizationRepository.existsById(orgToDelete.getId()));
	}

	@Test
	@WithMockUser(username = "testuser@example.com")
	void testGetMyPosts() throws Exception {
		JobPostResponseDto jobPost1 = JobPostResponseDto.builder()
				.postId(1L)
				.title("Software Engineer")
				.description("Job desc")
				.build();

		Mockito.when(jobPostInterface.getByOrganization(org.getId()))
				.thenReturn(ResponseEntity.ok(new ApiResponse<>(true, "Success", List.of(jobPost1))));

		mockMvc.perform(MockMvcRequestBuilders.get("/organizations/get-my-posts/{orgId}", org.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Software Engineer"));
	}
}
