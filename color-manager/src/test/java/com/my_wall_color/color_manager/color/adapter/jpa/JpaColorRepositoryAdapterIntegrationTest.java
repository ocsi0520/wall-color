package com.my_wall_color.color_manager.color.adapter.jpa;


import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.IntegrationTest;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    // TODO: should test also page metadata (number, size, total elements)
    @Test
    void shouldReturnThirdAndFourthColor() {
        var actual = unitUnderTest.findAll(new SortAndPagination<ColorField>(2, 1, SortOrderList.of()));
        assertThat(actual.getContent()).containsExactly(
                colorFixture.havasiEukaliptusz,
                colorFixture.szarkalab
        );
    }

    @Test
    void shouldReturnNothingDueToExceedingPageSize() {
        var actual = unitUnderTest.findAll(new SortAndPagination<ColorField>(5, 5, SortOrderList.of()));
        assertThat(actual.getContent()).isEmpty();
    }

    @Test
    void shouldReturnFourthAndThirdColor() {
        var sort = SortOrderList.of(new SortOrder<>(ColorField.NAME, SortOrder.Direction.DESCENDING));
        var actual = unitUnderTest.findAll(new SortAndPagination<>(2, 1, sort));
        assertThat(actual.getContent()).containsExactly(colorFixture.palastfu, colorFixture.kekSzelloRozsa);
    }

    @Test
    void shouldReturnAllColorByReverse() {
        var sort = SortOrderList.of(new SortOrder<>(ColorField.ID, SortOrder.Direction.DESCENDING));
        var actual = unitUnderTest.findAll(new SortAndPagination<>(10, 0, sort));

        assertThat(actual.getContent()).containsExactly(
                colorFixture.palastfu,
                colorFixture.kekSzelloRozsa,
                colorFixture.havasiGyopar,
                colorFixture.szarkalab,
                colorFixture.havasiEukaliptusz,
                colorFixture.brazilMenta,
                colorFixture.sulyom
        );
    }

    // TODO: rest of the tests
}