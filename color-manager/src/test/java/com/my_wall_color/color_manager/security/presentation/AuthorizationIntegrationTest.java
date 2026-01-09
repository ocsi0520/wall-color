package com.my_wall_color.color_manager.security.presentation;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationIntegrationTest extends IntegrationTest {
    @Autowired
    AuthTestHelper authTestHelper;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void injectUsers() {
        userFixture.injectAll();
    }

    @Test
    void shouldNotPassForNonAdminUser() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.alex));
        var responseForAdminResource = restTemplate.exchange("/api/for-admin", HttpMethod.GET, entity, String.class);
        assertThat(responseForAdminResource.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldPassForAdminUser() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        var responseForAdminResource = restTemplate.exchange("/api/for-admin", HttpMethod.GET, entity, String.class);
        assertThat(responseForAdminResource.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
