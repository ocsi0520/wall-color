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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActiveColorControllerIntegrationTest extends IntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    private ResponseEntity<List<Color>> getActiveColorsFor(User user) {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(user));
        return restTemplate.exchange("/api/me/active-color", HttpMethod.GET, authIncludedEntity, new ParameterizedTypeReference<>() {
        });
    }

    @Test
    void shouldReturnEmptyListForAlex() {
        var actual = getActiveColorsFor(userFixture.alex);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEmpty();
    }

    @Test
    void shouldReturnListForJdoe() {
        ResponseEntity<List<Color>> actual = getActiveColorsFor(userFixture.jdoe);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(colorFixture.getInitialAssignedColorsFor(userFixture.jdoe));
    }

    @Test
    void shouldNotAssignNonExistingColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + colorFixture.nonExistent.getId(), HttpMethod.POST, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotAssignAlreadyAssignedColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        var alreadyAssignedColor = colorFixture.getInitialAssignedColorsFor(userFixture.jdoe).getFirst();
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + alreadyAssignedColor.getId(), HttpMethod.POST, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldAssignValidColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.donna));
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + colorFixture.kekSzelloRozsa.getId(), HttpMethod.POST, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(getActiveColorsFor(userFixture.donna).getBody()).contains(colorFixture.kekSzelloRozsa);
    }

    @Test
    void shouldLimitAssignmentsTo7() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.alex));
        List<Color> allFixtureColors = colorFixture.getAllFixtureColors();
        var firstSevenColors = allFixtureColors.subList(0, 7);
        for (Color color : firstSevenColors) {
            ResponseEntity<Void> assignResponse = restTemplate.exchange("/api/me/active-color/" + color.getId(), HttpMethod.POST, authIncludedEntity, Void.class);
            assertThat(assignResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }
        var eigthColor = allFixtureColors.get(7);
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + eigthColor.getId(), HttpMethod.POST, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        for (Color color : firstSevenColors) {
            ResponseEntity<Void> revokeResponse = restTemplate.exchange("/api/me/active-color/" + color.getId(), HttpMethod.DELETE, authIncludedEntity, Void.class);
            assertThat(revokeResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }
    }

    @Test
    void shouldNotRevokeNonExistingColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + colorFixture.nonExistent.getId(), HttpMethod.DELETE, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotRevokeNonAssignedColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + colorFixture.kekSzelloRozsa.getId(), HttpMethod.DELETE, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldRevokeColor() {
        var authIncludedEntity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.donna));
        var firstAssignedColor = colorFixture.getInitialAssignedColorsFor(userFixture.donna).getFirst();
        ResponseEntity<Void> actual = restTemplate.exchange("/api/me/active-color/" + firstAssignedColor.getId(), HttpMethod.DELETE, authIncludedEntity, Void.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(getActiveColorsFor(userFixture.donna).getBody()).doesNotContain(firstAssignedColor);
    }
}