package com.jobconnect.job;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class JobControllerTest {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
		.withDatabaseName("jobconnect")
		.withUsername("jobconnect")
		.withPassword("jobconnect");

	@DynamicPropertySource
	static void configureDatasource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
		registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
		registry.add("spring.flyway.enabled", () -> true);
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JobRepository jobRepository;

	@BeforeEach
	void setUp() {
		jobRepository.deleteAll();
	}

	@Test
	void getJobs_ShouldReturnPagedJobs() throws Exception {
		Job job = new Job();
		job.setTitle("Senior Backend Engineer");
		job.setDescription("Build and scale APIs");
		job.setLocation("Remote");
		job.setSalaryMin(BigDecimal.valueOf(5000));
		job.setSalaryMax(BigDecimal.valueOf(8000));
		job.setEmploymentType(EmploymentType.FULL_TIME);
		job.setPostedBy("employer@example.com");
		jobRepository.save(job);

		mockMvc.perform(get("/api/v1/jobs").param("size", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].title").value("Senior Backend Engineer"));
	}
}
