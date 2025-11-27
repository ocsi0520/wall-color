package com.my_wall_color.color_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FrontendFilterTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldNotReturnHTMLForApi() {
        var asdResponse = restTemplate.getForEntity("/api/asd", String.class);
        assertThat(asdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(asdResponse.getHeaders().getContentType()).isNotEqualTo(MediaType.TEXT_HTML);
    }

    @Test
    void shouldNotReturnHTMLForStatic() {
        var iconResponse = restTemplate.getForEntity("/favicon.ico", String.class);
        assertThat(iconResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(iconResponse.getHeaders().getContentType().getType()).isEqualTo("image");
    }

    @Test
    void shouldReturnHTMLForRoot() {
        var rootResponse = restTemplate.getForEntity("/", String.class);
        assertThat(rootResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(rootResponse.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_HTML);
    }

    @Test
    void shouldReturnHTMLForIndexRoute() {
        var indexResponse = restTemplate.getForEntity("/index.html", String.class);
        assertThat(indexResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(indexResponse.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_HTML);
    }

    @Test
    void shouldReturnHTMLForFrontendRoute() {
        var frontendRouteResponse = restTemplate.getForEntity("/dummy/route", String.class);
        assertThat(frontendRouteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(frontendRouteResponse.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_HTML);
    }

    @Test
    void shouldNotReturnHTMLForNonExistentResource() {
        var nonExistentResourceResponse = restTemplate.getForEntity("/dummy/route.jpg", String.class);
        assertThat(nonExistentResourceResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}