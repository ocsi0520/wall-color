package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorCreationRequest;
import com.my_wall_color.color_manager.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.NestedTestConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.INHERIT)
class ColorControllerIntegrationCreateTest extends IntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @Value("${spring.data.web.pageable.max-page-index}")
    private Integer maxPageIndex;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    ColorCreationRequest validRequest = ColorCreationRequest.of(0, 0, 0, "black");

    @Test
    void shouldNotAllowInsertingExistingColor() {
        Color existingColor = colorFixture.sulyom;
        ColorCreationRequest existingColorRequest = new ColorCreationRequest(existingColor.getRed(), existingColor.getGreen(), existingColor.getBlue(), existingColor.getName());
        HttpHeaders authIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        var entity = new HttpEntity<>(existingColorRequest, authIncludedHeaders);
        ResponseEntity<Color> response = restTemplate.exchange("/api/color", HttpMethod.POST, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldNotAllowInsertingWithNonAdminUser() {
        HttpHeaders authIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.alex);
        var entity = new HttpEntity<>(validRequest, authIncludedHeaders);
        ResponseEntity<Color> response = restTemplate.exchange("/api/color", HttpMethod.POST, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldNotAllowInvalidColorComponentRequest() {
        HttpHeaders authIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        ColorCreationRequest invalidRedRequest = ColorCreationRequest.of(-1, 0, 0, "invalid red");
        var invalidRedEntity = new HttpEntity<>(invalidRedRequest, authIncludedHeaders);
        ResponseEntity<Color> invalidRedResponse = restTemplate.exchange("/api/color", HttpMethod.POST, invalidRedEntity, Color.class);
        assertThat(invalidRedResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void shouldNotAllowColorRequestWithEmptyName() {
        HttpHeaders authIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        ColorCreationRequest emptyNameRequest = ColorCreationRequest.of(0, 0, 0, null);
        var emptyNameEntity = new HttpEntity<>(emptyNameRequest, authIncludedHeaders);
        ResponseEntity<Color> emptyNameResponse = restTemplate.exchange("/api/color", HttpMethod.POST, emptyNameEntity, Color.class);
        assertThat(emptyNameResponse.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void shouldCreateNewColor() {
        HttpHeaders authIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
        var entity = new HttpEntity<>(validRequest, authIncludedHeaders);
        ResponseEntity<Color> response = restTemplate.exchange("/api/color", HttpMethod.POST, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        testEqualityBetweenColorAndRequest(response.getBody(), validRequest, userFixture.jdoe);

        var fetchedNewColorResponse = restTemplate.exchange(response.getHeaders().getLocation(), HttpMethod.GET, new HttpEntity<>(authIncludedHeaders), Color.class);
        assertThat(fetchedNewColorResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        testEqualityBetweenColorAndRequest(fetchedNewColorResponse.getBody(), validRequest, userFixture.jdoe);
    }

    private void testEqualityBetweenColorAndRequest(Color color, ColorCreationRequest request, User expectedCreator) {
        assertThat(color.getRed()).isEqualTo(request.red());
        assertThat(color.getGreen()).isEqualTo(request.green());
        assertThat(color.getBlue()).isEqualTo(request.blue());
        assertThat(color.getName()).isEqualTo(request.name());
        assertThat(color.getRecordedBy()).isEqualTo(expectedCreator.getId());
    }
}