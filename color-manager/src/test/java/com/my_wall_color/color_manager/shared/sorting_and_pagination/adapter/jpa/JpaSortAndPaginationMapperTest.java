package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.TestTranslationEnum;

import static com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.TestTranslationEnum.*;
import static org.assertj.core.api.Assertions.assertThat;

class JpaSortAndPaginationMapperTest {
    EnumToColumnMapper<TestTranslationEnum> testTranslationEnumEnumToColumnMapper = enumValue -> switch (enumValue) {
        case ONE -> "uno";
        case TWO -> "dos";
        case THREE -> "tres";
    };

    @Test
    void shouldHandleSimpleCase() {
        var unitUnderTest = new JpaSortAndPaginationMapper<TestTranslationEnum>(
                testTranslationEnumEnumToColumnMapper
        );
        var sorting = SortOrderList.of(
                new SortOrder<>(ONE, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TWO, SortOrder.Direction.DESCENDING),
                new SortOrder<>(THREE, SortOrder.Direction.ASCENDING)
        );

        SortAndPagination<TestTranslationEnum> sap = new SortAndPagination<>(5, 7, sorting);
        var actual = unitUnderTest.map(sap);
        var expected = PageRequest.of(7, 5,
                Sort.by(Order.asc("uno"), Order.desc("dos"), Order.asc("tres"))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldHandleEmptySortOrder() {
        var unitUnderTest = new JpaSortAndPaginationMapper<>(testTranslationEnumEnumToColumnMapper);
        SortOrderList<TestTranslationEnum> sorting = SortOrderList.of();
        SortAndPagination<TestTranslationEnum> sap = new SortAndPagination<>(5, 7, sorting);
        var actual = unitUnderTest.map(sap);
        var expected = PageRequest.of(7, 5);
        assertThat(actual).isEqualTo(expected);
    }
}