package com.my_wall_color.color_manager.security.presentation;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.my_wall_color.color_manager.security.token.CookieBearerTokenResolver.TOKEN_COOKIE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "token.cookie.domain=mozilla.org")
class AuthenticationControllerIntegrationTest extends IntegrationTest {
    private static final String JWT_REGEXP_PATTERN = "((?:\\.?(?:[A-Za-z0-9-_]+)){3})";

    @Value("${token.max-age-seconds}")
    private int EXPECTED_MAX_AGE_IN_SECONDS;

    @Autowired
    AuthTestHelper authTestHelper;

    @Autowired
    private Clock clock;

    @BeforeEach
    void injectUsers() {
        userFixture.injectAll();
    }

    @Test
    void shouldHandleLoginWithInvalidCredentials() {
        ResponseEntity<Void> response = authTestHelper.getWrongCredentialsAuthResponse();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldHandleLoginWithValidCredentials() {
        ResponseEntity<String> response = authTestHelper.getSuccessAuthResponse();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verifyLoginCookie(response);
        verifyLoginResponse(response);
    }

    private void verifyLoginResponse(ResponseEntity<String> response) {
        var body = response.getBody();
        assertThat(body).isNotEmpty();

        Instant parsedInstant = Instant.parse(body);
        Instant expectedAroundTime = Instant.now(clock).plus(Duration.ofSeconds(EXPECTED_MAX_AGE_IN_SECONDS));

        assertThat(expectedAroundTime.toEpochMilli() - parsedInstant.toEpochMilli()).isBetween(0L, 1000L);
    }

    private void verifyLoginCookie(ResponseEntity<String> response) {
        List<String> cookieTokens = response.getHeaders().get("set-cookie");
        assertThat(cookieTokens.size()).isGreaterThanOrEqualTo(1);
        String tokenCookieContent = cookieTokens.stream().filter(token -> token.startsWith(TOKEN_COOKIE_NAME)).findFirst().get();
        assertThat(tokenCookieContent).isNotNull();

        var cookieParts = tokenCookieContent.split("; ");
        assertThat(cookieParts.length).isGreaterThanOrEqualTo(3);
        assertThat(cookieParts).contains("Max-Age=" + EXPECTED_MAX_AGE_IN_SECONDS, "HttpOnly", "Domain=mozilla.org");
        assertThat(cookieParts[0]).matches(TOKEN_COOKIE_NAME + '=' + JWT_REGEXP_PATTERN);
    }
}