package com.fittrack.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fittrack.dto.request.LoginRequest;
import com.fittrack.dto.request.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Auth Integration Tests")
class AuthIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /auth/register — registers user and returns tokens")
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe.integration@fittrack.com");
        request.setPassword("StrongPass1!");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/auth/register", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("success")).isEqualTo(true);

        Map<?, ?> data = (Map<?, ?>) response.getBody().get("data");
        assertThat(data.get("accessToken")).isNotNull();
        assertThat(data.get("refreshToken")).isNotNull();
        assertThat(data.get("tokenType")).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("POST /auth/register — duplicate email returns 400")
    void register_duplicateEmail_returns400() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("duplicate.integration@fittrack.com");
        request.setPassword("StrongPass1!");

        restTemplate.postForEntity("/api/v1/auth/register", request, Map.class);
        // Register same email again
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/auth/register", request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("POST /auth/login — valid credentials return tokens")
    void login_success() {
        // First register
        RegisterRequest reg = new RegisterRequest();
        reg.setFirstName("Login");
        reg.setLastName("Test");
        reg.setEmail("login.test.integration@fittrack.com");
        reg.setPassword("StrongPass1!");
        restTemplate.postForEntity("/api/v1/auth/register", reg, Map.class);

        // Then login
        LoginRequest login = new LoginRequest();
        login.setEmail("login.test.integration@fittrack.com");
        login.setPassword("StrongPass1!");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<?, ?> data = (Map<?, ?>) response.getBody().get("data");
        assertThat(data.get("accessToken")).isNotNull();
    }

    @Test
    @DisplayName("POST /auth/login — wrong password returns 401")
    void login_wrongPassword_returns401() {
        LoginRequest login = new LoginRequest();
        login.setEmail("nonexistent@fittrack.com");
        login.setPassword("WrongPassword!");

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "/api/v1/auth/login", login, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("GET /users/me — without token returns 403")
    void getProfile_withoutToken_returns403() {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                "/api/v1/users/me", Map.class);

        assertThat(response.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN);
    }
}
