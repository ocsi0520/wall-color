package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WebSortAndPaginationMapperTest {
    LinkedHashMap<ColorField, Boolean> expectedDefaultSorting = new LinkedHashMap<>(Map.of(ColorField.ID, true));

    @Test
    void shouldMapWithoutSortToDefault() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(1, 5);
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(5, 1, expectedDefaultSorting);
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
                new LinkedHashMap<>(Map.of(ColorField.NAME, false))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithOneInvalidOrderToDefault() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(2, 10,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "non-existent-field"))
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(10, 2, expectedDefaultSorting);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithTwoValidOrdersToDefault() {
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
        var expected = new SortAndPagination<>(30, 3,
                new LinkedHashMap<>(Map.ofEntries(
                        Map.entry(ColorField.NAME, false)
                ))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapWithTwoInvalidOrders() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(3, 30,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "non-existent"),
                        new Sort.Order(Sort.Direction.DESC, "non-name")
                )
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(30, 3, expectedDefaultSorting);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldNotAcceptEmptyEnumClass() {
        enum EmptyFieldProvider implements FieldProvider {
            ;

            @Override
            public String getFieldName() {
                return "";
            }
        }

        assertThatThrownBy(() -> new WebSortAndPaginationMapper<>(EmptyFieldProvider.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Empty Enum");
    }

    @Test
    void shouldHandleExploitTryOnSort() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(100, 100,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "non-existent"),
                        new Sort.Order(Sort.Direction.DESC, "name"),
                        new Sort.Order(Sort.Direction.ASC, "name"),
                        new Sort.Order(Sort.Direction.DESC, "non-existent"),
                        new Sort.Order(Sort.Direction.ASC, "id"),
                        new Sort.Order(Sort.Direction.DESC, "id"),
                        new Sort.Order(Sort.Direction.DESC, "name"),
                        new Sort.Order(Sort.Direction.ASC, "name"),
                        new Sort.Order(Sort.Direction.ASC, "id"),
                        new Sort.Order(Sort.Direction.DESC, "id")
                )
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(100, 100,
                new LinkedHashMap<>(Map.ofEntries(
                        Map.entry(ColorField.NAME, true),
                        Map.entry(ColorField.ID, false)
                ))
        );
        assertThat(actual).isEqualTo(expected);
    }
}