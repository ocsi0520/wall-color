package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.LinkedHashMap;

import static com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa.TestTranslationEnum.*;
import static org.assertj.core.api.Assertions.assertThat;

enum TestTranslationEnum implements FieldProvider {
    ONE("egy"), TWO("kettő"), THREE("három");

    private final String fieldName;

    TestTranslationEnum(String egy) {
        this.fieldName = egy;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}

class JpaSortAndPaginationMapperTest {

    @Test
    void shouldHandleWithDefaultEnumToColumnMapper() {
        var unitUnderTest = new JpaSortAndPaginationMapper<TestTranslationEnum>(EnumToColumnMapper.getEnumValueMapper());
        LinkedHashMap<TestTranslationEnum, Boolean> sorting = new LinkedHashMap<>();
        sorting.put(ONE, true);
        sorting.put(TWO, false);
        sorting.put(THREE, true);
        SortAndPagination<TestTranslationEnum> sap = new SortAndPagination<>(5, 7, sorting);
        var actual = unitUnderTest.map(sap);
        var expected = PageRequest.of(7, 5,
                Sort.by(Order.asc("egy"), Order.desc("kettő"), Order.asc("három"))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldHandleWithCustomEnumToColumnMapper() {
        var unitUnderTest = new JpaSortAndPaginationMapper<TestTranslationEnum>(
                enumValue -> switch (enumValue) {
                    case ONE -> "uno";
                    case TWO -> "dos";
                    case THREE -> "tres";
                }
        );
        LinkedHashMap<TestTranslationEnum, Boolean> sorting = new LinkedHashMap<>();
        sorting.put(ONE, true);
        sorting.put(TWO, false);
        sorting.put(THREE, true);
        SortAndPagination<TestTranslationEnum> sap = new SortAndPagination<>(5, 7, sorting);
        var actual = unitUnderTest.map(sap);
        var expected = PageRequest.of(7, 5,
                Sort.by(Order.asc("uno"), Order.desc("dos"), Order.asc("tres"))
        );
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldHandleEmptyHashMap() {
        var unitUnderTest = new JpaSortAndPaginationMapper<TestTranslationEnum>(EnumToColumnMapper.getEnumValueMapper());
        LinkedHashMap<TestTranslationEnum, Boolean> sorting = new LinkedHashMap<>();
        SortAndPagination<TestTranslationEnum> sap = new SortAndPagination<>(5, 7, sorting);
        var actual = unitUnderTest.map(sap);
        var expected = PageRequest.of(7, 5);
        assertThat(actual).isEqualTo(expected);
    }
}