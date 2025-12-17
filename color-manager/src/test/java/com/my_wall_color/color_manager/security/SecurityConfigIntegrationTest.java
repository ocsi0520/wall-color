package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityConfigIntegrationTest extends IntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
    }

    @Test
    void shouldReturnUnauthenticatedForApiCallWithoutToken() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/asd", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnUnauthenticatedForApiCallWithWrongToken() {
        var invalidAuthIncludedHeaders = authTestHelper.getInvalidAuthIncludedHeaders();
        var entity = new HttpEntity<>(invalidAuthIncludedHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldSucceedApiCallWithCorrectToken() {
        var invalidAuthIncludedHeaders = authTestHelper.getValidAuthIncludedHeaders();
        var entity = new HttpEntity<>(invalidAuthIncludedHeaders);
        ResponseEntity<String> response = restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("asd");
    }

    @Test
    void shouldFetchStaticFileWithoutToken() {
        var iconResponse = restTemplate.getForEntity("/favicon.ico", String.class);
        assertThat(iconResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(iconResponse.getHeaders().getContentType().getType()).isEqualTo("image");
    }

    @Test
    void shouldFetchFrontendFromRootWithoutToken() {
        var rootResponse = restTemplate.getForEntity("/", String.class);
        assertThat(rootResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rootResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isTrue();
    }

    @Test
    void shouldFetchFrontendWithRouteWithoutToken() {
        var frontendRouteResponse = restTemplate.getForEntity("/dummy/route", String.class);
        assertThat(frontendRouteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(frontendRouteResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isTrue();
    }
}