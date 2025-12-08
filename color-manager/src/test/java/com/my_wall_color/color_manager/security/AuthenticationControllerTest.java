package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.adapter.JpaColor;
import com.my_wall_color.test_utils.PostgresContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthenticationControllerTest {
    private static final String JWT_REGEXP_PATTERN = "((?:\\.?(?:[A-Za-z0-9-_]+)){3})";

    // TODO: de-duplicate with PostgresContainerTest, in case using it currently ending up w/
    //  HikariPool-1 - Failed to validate connection org.postgresql.jdbc.PgConnection@6f0df74 (This connection has been closed.). Possibly consider using a shorter maxLifetime value.
    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18-alpine")
                    .withReuse(true);

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void reachPublicEndpoint() {
        var response = restTemplate.postForEntity("/api/public", null, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello World");
    }

    @Test
    void loginWithCredentials() {
        LoginRequest loginRequest = new LoginRequest("jdoe", "user1");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        testLoginCookie(response);
        testLoginResponse(response);
    }

    private void testLoginResponse(ResponseEntity<String> response) {
        assertThat(response.getBody()).matches(JWT_REGEXP_PATTERN);
    }

    private void testLoginCookie(ResponseEntity<String> response) {
        List<String> cookieTokens = response.getHeaders().get("set-cookie");
        assertThat(cookieTokens).hasSize(1);
        String tokenCookieContent = cookieTokens.getFirst();
        System.out.println(tokenCookieContent);
        var cookieParts = tokenCookieContent.split("; ");
        assertThat(cookieParts.length).isGreaterThanOrEqualTo(3);
        assertThat(cookieParts).contains("Max-Age=3600", "HttpOnly");
        assertThat(cookieParts[0]).matches("token=" + JWT_REGEXP_PATTERN);
    }

    @Test
    void getColor() {
        var response = restTemplate.getForEntity("/api/color/1", JpaColor.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}