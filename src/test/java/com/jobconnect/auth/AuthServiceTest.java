package com.jobconnect.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.jobconnect.auth.dto.AuthResponse;
import com.jobconnect.auth.dto.RegisterRequest;
import com.jobconnect.user.UserRepository;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class AuthServiceTest {

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
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void cleanUp() {
		userRepository.deleteAll();
	}

	@Test
	void register_ShouldCreateUserAndReturnTokens() {
		String email = UUID.randomUUID() + "@example.com";
		RegisterRequest request = new RegisterRequest(email, "Password123!", "Test User");

		AuthResponse response = authService.register(request);

		assertThat(response.accessToken()).isNotBlank();
		assertThat(response.refreshToken()).isNotBlank();
		assertThat(userRepository.existsByEmailIgnoreCase(email)).isTrue();
	}
}
