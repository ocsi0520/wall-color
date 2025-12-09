package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.adapter.JpaColor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.my_wall_color.color_manager.security.CookieBearerTokenResolver.TOKEN_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthenticationControllerTest {
    private static final String JWT_REGEXP_PATTERN = "((?:\\.?(?:[A-Za-z0-9-_]+)){3})";
    private static final int EXPECTED_MAX_AGE_IN_SECONDS = 3600;

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

    @Autowired
    private Clock clock;

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
        var body = response.getBody();
        assertThat(body).isNotEmpty();

        Instant parsedInstant = Instant.parse(body);
        Instant expectedAroundTime = Instant.now(clock).plus(Duration.ofSeconds(EXPECTED_MAX_AGE_IN_SECONDS));

        assertThat(expectedAroundTime.toEpochMilli() - parsedInstant.toEpochMilli()).isBetween(0L, 1000L);
    }

    private void testLoginCookie(ResponseEntity<String> response) {
        List<String> cookieTokens = response.getHeaders().get("set-cookie");
        assertThat(cookieTokens.size()).isGreaterThanOrEqualTo(1);
        String tokenCookieContent = cookieTokens.stream().filter(token -> token.startsWith(TOKEN_COOKIE_NAME)).findFirst().get();
        assertThat(tokenCookieContent).isNotNull();

        var cookieParts = tokenCookieContent.split("; ");
        assertThat(cookieParts.length).isGreaterThanOrEqualTo(3);
        assertThat(cookieParts).contains("Max-Age=" + EXPECTED_MAX_AGE_IN_SECONDS, "HttpOnly");
        assertThat(cookieParts[0]).matches(TOKEN_COOKIE_NAME + '=' + JWT_REGEXP_PATTERN);
    }

    @Test
    void getColor() {
        var response = restTemplate.getForEntity("/api/color/1", JpaColor.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}