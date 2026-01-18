package com.my_wall_color.color_manager.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

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
    ResponseEntity<String> response =
        restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  void shouldSucceedApiCallWithCorrectToken() {
    var validAuthIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
    var entity = new HttpEntity<>(validAuthIncludedHeaders);
    ResponseEntity<String> response =
        restTemplate.exchange("/api/asd", HttpMethod.GET, entity, String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo("asd");
  }

  static Stream<Arguments> getOldFrontendEndpoints() {
    return Stream.of(
        Arguments.of("/favicon.ico"),
        Arguments.of("/"),
        Arguments.of("/dummy/route")
    );
  }

  @ParameterizedTest
  @MethodSource("getOldFrontendEndpoints")
  void shouldBlockOldFrontendEndpointsWithoutToken(String path) {
    var responseToOldEndpoint = restTemplate.getForEntity(path, String.class);
    assertThat(responseToOldEndpoint.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @ParameterizedTest
  @MethodSource("getOldFrontendEndpoints")
  void shouldReturnNotFoundForFrontendEndpointsWithToken(String path) {
    var validAuthIncludedHeaders = authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe);
    var entity = new HttpEntity<>(validAuthIncludedHeaders);
    ResponseEntity<String> responseToOldEndpoint =
        restTemplate.exchange(path, HttpMethod.GET, entity, String.class);
    assertThat(responseToOldEndpoint.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}