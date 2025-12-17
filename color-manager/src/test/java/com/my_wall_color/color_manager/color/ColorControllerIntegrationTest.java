package com.my_wall_color.color_manager.color;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.user.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ColorControllerIntegrationTest extends IntegrationTest {
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
    public void shouldReturnNotFound() {
        var entity = new HttpEntity<>(authTestHelper.getValidAuthIncludedHeaders());
        ResponseEntity<Color> response = restTemplate.exchange("/api/color/" + colorFixture.nonExistent.getId(), HttpMethod.GET, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnSulyomColor() {
        var entity = new HttpEntity<>(authTestHelper.getValidAuthIncludedHeaders());
        ResponseEntity<Color> response = restTemplate.exchange("/api/color/" + colorFixture.sulyom.getId(), HttpMethod.GET, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(colorFixture.sulyom);
    }
}