package com.my_wall_color.color_manager.security;

import com.my_wall_color.test_utils.PostgresContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigTest implements PostgresContainerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnUnauthenticated() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/asd", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnString() {
        HttpEntity<String> entity = getHttpEntityWith(retrieveToken());
        ResponseEntity<String> response = restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("asd");
    }

    private String retrieveToken() {
        LoginRequest loginRequest = new LoginRequest("jdoe", "user1");
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
        return tokenResponse.getBody();
    }

    private <T> HttpEntity<T> getHttpEntityWith(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<T>(headers);
    }
}