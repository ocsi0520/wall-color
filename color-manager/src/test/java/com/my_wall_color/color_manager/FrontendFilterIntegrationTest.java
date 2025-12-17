package com.my_wall_color.color_manager;

import com.my_wall_color.test_utils.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FrontendFilterIntegrationTest extends IntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @Test
    void shouldNotReturnHTMLForApi() {
        var entity = new HttpEntity<>(authTestHelper.getValidAuthIncludedHeaders());
        ResponseEntity<String> response = restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isFalse();
        assertThat(response.getHeaders().getContentType().includes(MediaType.TEXT_PLAIN)).isTrue();
    }

    @Test
    void shouldNotReturnHTMLForStaticIcon() {
        var iconResponse = restTemplate.getForEntity("/favicon.ico", String.class);
        assertThat(iconResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(iconResponse.getHeaders().getContentType().getType()).isEqualTo("image");
    }

    @Test
    void shouldReturnHTMLForRoot() {
        var rootResponse = restTemplate.getForEntity("/", String.class);
        assertThat(rootResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rootResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isTrue();
    }

    @Test
    void shouldReturnHTMLForIndexRoute() {
        var indexResponse = restTemplate.getForEntity("/index.html", String.class);
        assertThat(indexResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(indexResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isTrue();
    }

    @Test
    void shouldReturnHTMLForFrontendRoute() {
        var frontendRouteResponse = restTemplate.getForEntity("/dummy/route", String.class);
        assertThat(frontendRouteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(frontendRouteResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isTrue();
    }

    @Test
    void shouldNotReturnHTMLForNonExistentResource() {
        var nonExistentResourceResponse = restTemplate.getForEntity("/dummy/route.jpg", String.class);
        assertThat(nonExistentResourceResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}