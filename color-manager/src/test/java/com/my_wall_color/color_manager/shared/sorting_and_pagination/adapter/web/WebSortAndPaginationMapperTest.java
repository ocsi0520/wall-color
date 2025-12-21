package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class WebSortAndPaginationMapperTest {

    @Test
    void shouldMapWithoutSort() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(1, 5);
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(5, 1, new LinkedHashMap<>());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithOneValidOrder() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(2, 10,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "name"))
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(10, 2,
                new LinkedHashMap<>(Map.ofEntries(Map.entry(ColorField.NAME, false)))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithOneInvalidOrder() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(2, 10,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "non-existent-field"))
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(10, 2, new LinkedHashMap<>());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithTwoValidOrders() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(3, 30,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "id"),
                        new Sort.Order(Sort.Direction.DESC, "name")
                )
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(30, 3,
                new LinkedHashMap<>(Map.ofEntries(
                        Map.entry(ColorField.ID, true),
                        Map.entry(ColorField.NAME, false)
                ))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithOneValidAndOneInvalidOrder() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(3, 30,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "non-existent"),
                        new Sort.Order(Sort.Direction.DESC, "name")
                )
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(30, 3,
                new LinkedHashMap<>(Map.ofEntries(
                        Map.entry(ColorField.NAME, false)
                ))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithTwoInvalidOrder() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(3, 30,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "non-existent"),
                        new Sort.Order(Sort.Direction.DESC, "non-name")
                )
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(30, 3,
                new LinkedHashMap<>()
        );
        assertThat(actual).isEqualTo(expected);
    }
}