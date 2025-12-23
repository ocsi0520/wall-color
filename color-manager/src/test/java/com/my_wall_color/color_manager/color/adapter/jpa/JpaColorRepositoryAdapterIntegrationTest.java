package com.my_wall_color.color_manager.color.adapter.jpa;


import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaColorRepositoryAdapterIntegrationTest extends IntegrationTest {
    @Autowired
    private JpaColorRepositoryAdapter unitUnderTest;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    @Test
    void shouldReturnSulyom() {
        assertThat(unitUnderTest.findById(colorFixture.sulyom.getId())).contains(colorFixture.sulyom);
    }

    @Test
    void shouldReturnEmptyColor() {
        assertThat(unitUnderTest.findById(colorFixture.nonExistent.getId())).isEmpty();
    }

    @Test
    void shouldReturnEmptyColorList() {
        assertThat(unitUnderTest.findAllAssociatedWith(userFixture.nonExistent.getId())).isEmpty();
    }

    @Test
    void shouldReturn3ColorsForJdoeUser() {
        List<Color> colorsForJdoeUser = unitUnderTest.findAllAssociatedWith(userFixture.jdoe.getId());
        assertThat(colorsForJdoeUser).isEqualTo(List.of(colorFixture.sulyom, colorFixture.havasiGyopar));
    }

    @Test
    void shouldReturnThirdAndFourthColor() {
        var actual = unitUnderTest.findAll(new SortAndPagination<>(2, 1, new LinkedHashMap<>()));
        assertThat(actual).containsExactly(
                colorFixture.kekSzelloRozsa,
                colorFixture.szarkalab
        );
    }

    @Test
    void shouldReturnNothingDueToExceedingPageSize() {
        var actual = unitUnderTest.findAll(new SortAndPagination<>(5, 5, new LinkedHashMap<>()));
        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnFourthAndThirdColor() {
        var sort = new LinkedHashMap<>(Map.of(ColorField.NAME, false));
        var actual = unitUnderTest.findAll(new SortAndPagination<>(2, 1, sort));
        assertThat(actual).containsExactly(colorFixture.palastfu, colorFixture.kekSzelloRozsa);
    }

    @Test
    void shouldReturnAllColorByReverse() {
        var sort = new LinkedHashMap<>(Map.of(ColorField.ID, false));
        var actual = unitUnderTest.findAll(new SortAndPagination<>(10, 0, sort));
        assertThat(actual).containsExactly(
                colorFixture.palastfu,
                colorFixture.havasiEukaliptusz,
                colorFixture.havasiGyopar,
                colorFixture.szarkalab,
                colorFixture.kekSzelloRozsa,
                colorFixture.brazilMenta,
                colorFixture.sulyom
        );
    }
}