package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WebSortAndPaginationMapperTest {
    SortOrderList<ColorField> expectedDefaultSorting = SortOrderList.of(new SortOrder<>(ColorField.ID, SortOrder.Direction.ASCENDING));

    private <T extends Enum<T> & FieldProvider> void testEqualityOfSAP(SortAndPagination<T> actual, SortAndPagination<T> expected) {
        assertThat(actual.getPageIndex()).isEqualTo(expected.getPageIndex());
        assertThat(actual.getPageSize()).isEqualTo(expected.getPageSize());
        assertThat(actual.getSorting().getOrderList()).isEqualTo(expected.getSorting().getOrderList());
    }

    @Test
    void shouldMapWithoutSortToDefault() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(1, 5);
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(5, 1, expectedDefaultSorting);
        testEqualityOfSAP(actual, expected);
    }

    @Test
    void shouldMapWithOneValidOrder() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(2, 10,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "name"))
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<ColorField>(10, 2,
                SortOrderList.of(new SortOrder<>(ColorField.NAME, SortOrder.Direction.DESCENDING))
        );
        testEqualityOfSAP(actual, expected);
    }

    @Test
    void shouldMapWithOneInvalidOrderToDefault() {
        var unitUnderTest = new WebSortAndPaginationMapper<>(ColorField.class);
        Pageable pageRequestWithoutSort = PageRequest.of(2, 10,
                Sort.by(new Sort.Order(Sort.Direction.DESC, "non-existent-field"))
        );
        var actual = unitUnderTest.map(pageRequestWithoutSort);
        var expected = new SortAndPagination<>(10, 2, expectedDefaultSorting);
        testEqualityOfSAP(actual, expected);
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
                SortOrderList.of(
                        new SortOrder<>(ColorField.ID, SortOrder.Direction.ASCENDING),
                        new SortOrder<>(ColorField.NAME, SortOrder.Direction.DESCENDING)
                )
        );
        testEqualityOfSAP(actual, expected);
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
                SortOrderList.of(
                        new SortOrder<>(ColorField.NAME, SortOrder.Direction.DESCENDING)
                )
        );
        testEqualityOfSAP(actual, expected);
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
        testEqualityOfSAP(actual, expected);
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
                SortOrderList.of(
                        new SortOrder<>(ColorField.NAME, SortOrder.Direction.ASCENDING),
                        new SortOrder<>(ColorField.ID, SortOrder.Direction.DESCENDING)
                )
        );
        testEqualityOfSAP(actual, expected);
    }
}