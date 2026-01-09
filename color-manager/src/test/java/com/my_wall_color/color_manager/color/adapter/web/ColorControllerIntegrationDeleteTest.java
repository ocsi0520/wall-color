package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ColorControllerIntegrationDeleteTest extends IntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    @Test
    void shouldReturnNotFoundForNonExistentColor() {
        var headerWithAuth = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        var entity = new HttpEntity<>(headerWithAuth);
        var response = restTemplate.exchange("/api/color/" + colorFixture.nonExistent.getId(), HttpMethod.DELETE, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotBeAbleToDeleteWithoutAdminRole() {
        var headerWithAuth = authTestHelper.getAuthIncludedHeadersFor(userFixture.alex);
        var entity = new HttpEntity<>(headerWithAuth);
        var response = restTemplate.exchange("/api/color/" + colorFixture.sulyom.getId(), HttpMethod.DELETE, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldBeAbleToDeleteColor() {
        var headerWithAuth = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        var entity = new HttpEntity<>(headerWithAuth);
        Color sulyom = colorFixture.sulyom;
        var deleteResponse = restTemplate.exchange("/api/color/" + sulyom.getId(), HttpMethod.DELETE, entity, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        var fetchResponse = restTemplate.exchange("/api/color/" + sulyom.getId(), HttpMethod.GET, entity, Color.class);
        assertThat(fetchResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        testUserHasAssignmentToColor(userFixture.jdoe, sulyom);
        testUserHasAssignmentToColor(userFixture.donna, sulyom);
        testUserHasAssignmentToColor(userFixture.alex, sulyom);
    }

    private void testUserHasAssignmentToColor(User user, Color deletedColor) {
        // TODO: uncomment after /api/me/active-color is implemented
//        var headerWithAuth = authTestHelper.getAuthIncludedHeadersFor(user);
//        var entity = new HttpEntity<>(headerWithAuth);
//        ResponseEntity<List<Color>> assignedColorsResponse = restTemplate.exchange("/api/me/active-color/", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
//        });
//        var colorStillAssigned = assignedColorsResponse.getBody().stream().anyMatch(color -> deletedColor.getId().equals(color.getId()));
//        assertThat(colorStillAssigned).isFalse();
    }
}