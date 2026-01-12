package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.AuthTestHelper;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ColorControllerIntegrationReadTest extends IntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    AuthTestHelper authTestHelper;

    @Value("${spring.data.web.pageable.max-page-index}")
    private Integer maxPageIndex;

    private @NotNull List<Color> getAllColorsInInsertionOrder() {
        return colorFixture.getAllFixtureColors().stream().sorted(Comparator.comparing(Color::getId)).toList();
    }

    private @NotNull List<Color> getAllColorsSortedByName() {
        return colorFixture.getAllFixtureColors().stream().sorted(Comparator.comparing(Color::getName)).toList();
    }

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    @Test
    public void shouldReturnNotFound() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<Color> response = restTemplate.exchange("/api/color/" + colorFixture.nonExistent.getId(), HttpMethod.GET, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private String generateSearchQuery(int pageIndex, int pageSize, String... sortOrders) {
        return generateSearchQuery(String.valueOf(pageIndex), String.valueOf(pageSize), sortOrders);
    }

    private String generateSearchQuery(String pageIndex, String pageSize, String... sortOrders) {
        var sort = Arrays.stream(sortOrders).map(order -> "sort=" + order).collect(Collectors.joining("&"));
        return "?page=" + pageIndex + "&size=" + pageSize + '&' + sort;
    }


    @Test
    public void shouldReturnSulyomColor() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<Color> response = restTemplate.exchange("/api/color/" + colorFixture.sulyom.getId(), HttpMethod.GET, entity, Color.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(colorFixture.sulyom);
    }

    @Test
    public void shouldReturnLast3ColorsByName() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<PageDTO<Color>> response = restTemplate.exchange("/api/color?page=0&size=3&sort=name,desc", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        var expectedContent = List.of(colorFixture.szarkalab, colorFixture.sulyom, colorFixture.palastfu);
        var expected = new PageDTO<>(expectedContent, 0, 3, 8, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnDefaultPage() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        ResponseEntity<PageDTO<Color>> response = restTemplate.exchange("/api/color", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        var expected = new PageDTO<>(getAllColorsInInsertionOrder(), 0, 20, 8, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldHandleTooBigPage() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        var searchQuery = generateSearchQuery(0, Integer.MAX_VALUE, "name,desc");
        ResponseEntity<PageDTO<Color>> response = restTemplate.exchange(
                "/api/color" + searchQuery, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        var expected = new PageDTO<>(getAllColorsSortedByName().reversed(), 0, 100, 8, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldHandleFirstExploitTryProperly() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        var suspiciousSearchQuery = generateSearchQuery("9999999999999999", "9999999999999999",
                "id,desc",
                "name,desc",
                "id",
                "id,asc",
                "id",
                "non-existent",
                "id,desc",
                "id",
                "id",
                "name,asc",
                "id,desc"
        );


        ResponseEntity<PageDTO<Color>> response = restTemplate.exchange(
                "/api/color" + suspiciousSearchQuery, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        var expected = new PageDTO<>(getAllColorsInInsertionOrder().reversed(), 0, 20, 8, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldHandleSecondExploitTryProperly() {
        var entity = new HttpEntity<>(authTestHelper.getAuthIncludedHeadersFor(userFixture.jdoe));
        int max_int = Integer.MAX_VALUE;
        var suspiciousSearchQuery = generateSearchQuery(max_int, max_int,
                "id,desc",
                "name,desc",
                "id",
                "id,asc",
                "id",
                "non-existent",
                "id,desc",
                "id",
                "id,",
                "name,asc",
                "id,desc"
        );
        ResponseEntity<PageDTO<Color>> response = restTemplate.exchange(
                "/api/color" + suspiciousSearchQuery, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var actual = response.getBody();
        var expected = new PageDTO<>(List.of(), maxPageIndex, 100, 8, 1);
        assertThat(actual).isEqualTo(expected);
    }

}