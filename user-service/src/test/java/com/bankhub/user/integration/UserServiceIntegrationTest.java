package com.bankhub.user.integration;

import com.bankhub.user.dto.UserRegistrationDto;
import com.bankhub.user.entity.User;
import com.bankhub.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class UserServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerUser_Success() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("integrationuser");
        dto.setEmail("integration@test.com");
        dto.setPassword("password123");
        dto.setFirstName("Integration");
        dto.setLastName("Test");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/users/register", dto, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        User savedUser = userRepository.findByUsername("integrationuser").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("integration@test.com");
    }
}
