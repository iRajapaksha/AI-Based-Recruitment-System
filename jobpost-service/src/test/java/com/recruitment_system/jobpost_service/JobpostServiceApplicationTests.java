package com.recruitment_system.jobpost_service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recruitment_system.jobpost_service.dto.ApiResponse;
import com.recruitment_system.jobpost_service.dto.JobPostResponseDto;
import com.recruitment_system.jobpost_service.dto.JobPostUpdateDto;
import com.recruitment_system.jobpost_service.feign.OrganizationInterface;
import com.recruitment_system.jobpost_service.model.JobPost;
import com.recruitment_system.jobpost_service.model.Skill;
import com.recruitment_system.jobpost_service.repository.JobPostRepository;
import com.recruitment_system.jobpost_service.repository.SkillRepository;
import com.recruitment_system.jobpost_service.service.JobPostService;
import org.apache.kafka.common.protocol.types.ArrayOf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.GenericContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportTestcontainers
@AutoConfigureMockMvc
class JobpostServiceApplicationTests {

	@ServiceConnection
	static GenericContainer genericContainer = new GenericContainer(
			DockerImageName.parse("redis:latest"))
			.withExposedPorts(6379);
	@Autowired
	private MockMvc mockMvc;
	@MockitoBean
	private OrganizationInterface organizationInterface;
	@Autowired
	private JobPostRepository jobPostRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private JobPostService jobPostService;
	@Autowired
	private CacheManager cacheManager;
	@MockitoSpyBean
	private JobPostRepository spyJobPostRepository;

	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


	@BeforeEach
	void setUp() {

		jobPostRepository.deleteAll();
	}

	private Skill getOrCreateSkill(String name) {
		return skillRepository.findByName(name)
				.orElseGet(() -> skillRepository.save(new Skill(name)));
	}

	private JobPost createSampleJobPost() {
		JobPost jobPost = new JobPost();
		jobPost.setTitle("Data Scientist");
		jobPost.setDescription("Data Science Job description here");
		jobPost.setCurrency("USD");
		jobPost.setBenefits("whatever");
		jobPost.setRequirements("Strong background in ML, TensorFlow, and PyTorch.");
		jobPost.setDeadline(LocalDateTime.now().plusDays(30));
		jobPost.setIsActive(true);
		jobPost.setIsDraft(false);
		jobPost.setCompanyName("DataCorp");
		jobPost.setLocation("New York");
		jobPost.setWorkType("On-site");
		jobPost.setExperienceLevel("Senior");
		jobPost.setEmploymentType("Full-time");
		jobPost.setMinSalary(90000);
		jobPost.setMaxSalary(130000);
		jobPost.setOrgId(1L);

		List<Skill> skills = List.of(
				getOrCreateSkill("Java"),
				getOrCreateSkill("Spring Boot")
		);
		jobPost.setSkills(skills);

		return jobPostRepository.save(jobPost);
	}


	@Test
	@WithMockUser(username = "testuser", roles = {"RECRUITER"})
	void testCreateJobpostAndCacheIt() throws Exception {

		//Mock organization service response
		Mockito.when(organizationInterface.getLogo(1L))
				.thenReturn(ResponseEntity.ok("mocked-logo-url.png"));

		JobPost jobPost = createSampleJobPost();

		//Create a jobpost
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/job-posts/create")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(jobPost))
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		ApiResponse<JobPostResponseDto> apiResponse = objectMapper.readValue(
				responseContent,
				new TypeReference<ApiResponse<JobPostResponseDto>>() {
				});

		JobPostResponseDto createdJobPost = apiResponse.getData();
		Long createdJobPostId = createdJobPost.getPostId();

		//Check post exists in DB
		Assertions.assertTrue(jobPostRepository.findById(createdJobPostId).isPresent());

		//Check cache
		Cache cache = cacheManager.getCache("JOBPOST_CACHE");
		Object cached = cache.get(createdJobPostId).get();
		JobPostResponseDto dto = objectMapper.convertValue(cached, JobPostResponseDto.class);
		Assertions.assertNotNull(dto);

	}

	@Test
	@WithMockUser(username = "testuser", roles = {"RECRUITER"})
	void testGetJobpostAndVerifyCache() throws Exception{

		//Save the jobpost directly to repository
		JobPost jobPost = new JobPost();
		jobPost.setTitle("Data Scientist");
		jobPost.setDescription("Data Science Job description here");
		jobPost.setCurrency("USD");
		jobPost.setDeadline(java.time.LocalDateTime.now().plusDays(30));
		jobPost.setCreatedAt(java.time.LocalDateTime.now());
		jobPost.setIsActive(true);
		jobPost.setIsDraft(false);
		jobPost.setCompanyName("DataCorp");
		jobPost.setLocation("New York");
		jobPost.setWorkType("Onsite");
		jobPost.setExperienceLevel("Senior");
		jobPost.setEmploymentType("Full-time");
		jobPost.setMinSalary(90000);
		jobPost.setMaxSalary(130000);
		jobPost.setOrgId(2L);

		jobPostRepository.save(jobPost);

		//Fetch the jobpost via API
		mockMvc.perform(
				org.springframework.test.web.servlet.request.MockMvcRequestBuilders
						.get("/job-posts/get/" + jobPost.getId())
						.contentType("application/json")
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(jobPost.getId()))
				.andReturn();

		// Inspect cache after first fetch
		Cache cache = cacheManager.getCache("JOBPOST_CACHE");
		Assertions.assertNotNull(cache);

		Object cached = cache.get(jobPost.getId()).get();
		JobPostResponseDto cachedDto = objectMapper.convertValue(cached, JobPostResponseDto.class);

		Assertions.assertNotNull(cachedDto);
		Assertions.assertEquals(jobPost.getId(), cachedDto.getPostId());


		//Verify that the repository method was called only once (due to caching)
		Mockito.verify(spyJobPostRepository, Mockito.times(1)).findById(jobPost.getId());

		Mockito.clearInvocations(spyJobPostRepository);

		//Fetch the jobpost again via API
		mockMvc.perform(
						org.springframework.test.web.servlet.request.MockMvcRequestBuilders
								.get("/job-posts/get/" + jobPost.getId())
								.contentType("application/json")
				)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(jobPost.getId()))
				.andReturn();
		// Inspect cache manually
		Cache cache2 = cacheManager.getCache("JOBPOST_CACHE");
		Object cached2 = cache2.get(jobPost.getId()).get();
		JobPostResponseDto cachedDto2 = objectMapper.convertValue(cached, JobPostResponseDto.class);

		Assertions.assertEquals(jobPost.getId(), cachedDto2.getPostId());

		//Verify that the repository method was NOT called again (due to caching)
		Mockito.verify(spyJobPostRepository, Mockito.times(0)).findById(jobPost.getId());
	}

	@Test
	@WithMockUser(username = "USER", roles = {"RECRUITER"})
	void testUpdateJobpostAndVerifyCache() throws Exception{

		//Save the jobpost directly to repository
		JobPost jobPost = createSampleJobPost();

		//Fetch the jobpost via API to populate cache
		mockMvc.perform(
				MockMvcRequestBuilders
						.get("/job-posts/get/" + jobPost.getId())
						.contentType("application/json")
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(jobPost.getId()))
				.andReturn();

		// Inspect cache after first fetch
		Cache cache = cacheManager.getCache("JOBPOST_CACHE");
		Assertions.assertNotNull(cache);
		JobPostResponseDto cachedJobPost = cache.get(jobPost.getId(), JobPostResponseDto.class);
		Assertions.assertNotNull(cachedJobPost);
		Assertions.assertEquals("Data Scientist", cachedJobPost.getTitle());

		//Update jobpost via API
		JobPostUpdateDto jobPostUpdateDto = new JobPostUpdateDto();
		jobPostUpdateDto.setTitle("Senior DevOps Engineer Updated");
		jobPostUpdateDto.setDescription("Updated DevOps Job description here Updated");

		//Update the jobpost via API
		mockMvc.perform(
				MockMvcRequestBuilders
						.patch("/job-posts/update/" + jobPost.getId())
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(jobPostUpdateDto))
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//Fetch the jobpost again via API (should reflect update — cache invalidated or refreshed)
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders
						.get("/job-posts/get/" + jobPost.getId())
						.contentType("application/json")
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();


		String responseContent = result.getResponse().getContentAsString();
		ApiResponse<JobPostResponseDto> apiResponse = objectMapper.readValue(
				responseContent,
				new TypeReference<ApiResponse<JobPostResponseDto>>() {
				});
		JobPostResponseDto updatedJobPost = apiResponse.getData();

		//Verify that the title is updated in the cache
		Assertions.assertEquals("Senior DevOps Engineer Updated", updatedJobPost.getTitle());

		// Verify cache now holds updated entry
		JobPostResponseDto cachedAfter = cache.get(jobPost.getId(), JobPostResponseDto.class);
		Assertions.assertNotNull(cachedAfter);
		Assertions.assertEquals("Senior DevOps Engineer Updated", cachedAfter.getTitle());
	}

	@Test
	@WithMockUser(username = "USER", roles = {"RECRUITER"})
	void testDeleteJobpostAndEvictCache() throws Exception{
		//Save the jobpost directly to repository
		JobPost jobPost = createSampleJobPost();

		//Fetch the jobpost via API to populate cache
		mockMvc.perform(
				MockMvcRequestBuilders
						.get("/job-posts/get/" + jobPost.getId())
						.contentType("application/json")
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.postId").value(jobPost.getId()))
				.andReturn();

		// Inspect cache after first fetch
		Cache cache = cacheManager.getCache("JOBPOST_CACHE");
		Assertions.assertNotNull(cache);
		JobPostResponseDto cachedJobPost = cache.get(jobPost.getId(), JobPostResponseDto.class);
		Assertions.assertNotNull(cachedJobPost);
		Assertions.assertEquals("Data Scientist", cachedJobPost.getTitle());

		//Delete the jobpost via API
		mockMvc.perform(
				MockMvcRequestBuilders
						.delete("/job-posts/delete/" + jobPost.getId())
						.contentType("application/json")
		)		.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		//Check that the jobpost is deleted from DB
		Assertions.assertFalse(jobPostRepository.findById(jobPost.getId()).isPresent());

		//Verify that the cache is evicted
		Cache cacheAfterDeletion = cacheManager.getCache("JOBPOST_CACHE");
		Assertions.assertNotNull(cacheAfterDeletion);
		Assertions.assertNull(cacheAfterDeletion.get(jobPost.getId(), JobPostResponseDto.class));
	}

}
