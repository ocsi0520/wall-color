package com.my_wall_color.color_manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: maybe we don't need a separate NoContextTestApp
//  these can be tested w/ production SecurityFilterChain
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.my_wall_color.test_utils.NoContextTestApp.class,
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
        }
)
@Import(FrontendFilterTest.TestConfig.class)
class FrontendFilterTest {
    @Autowired
    TestRestTemplate restTemplate;

    @TestConfiguration
    @Import({DummyController.class, FrontendFilter.class})
    static class TestConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(CsrfConfigurer::disable)
                    .authorizeHttpRequests(authz -> authz.anyRequest().permitAll()).build();
        }
    }

    @Test
    void shouldNotReturnHTMLForApi() {
        var asdResponse = restTemplate.getForEntity("/api/asd", String.class);
        assertThat(asdResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(asdResponse.getHeaders().getContentType().includes(MediaType.TEXT_HTML)).isFalse();
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