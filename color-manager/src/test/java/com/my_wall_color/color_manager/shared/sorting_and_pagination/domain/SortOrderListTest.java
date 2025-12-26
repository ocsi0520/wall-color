package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SortOrderListTest {
    @Test
    void shouldHandleEmptyList() {
        var actual = SortOrderList.of();
        assertThat(actual.getOrderList()).isEmpty();
    }

    @Test
    void shouldHandleOneElement() {
        var actual = SortOrderList.of(new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING));
        var expected = List.of(new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING));
        assertThat(actual.getOrderList()).isEqualTo(expected);
    }

    @Test
    void shouldHandleMultipleElements() {
        var actual = SortOrderList.of(
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.DESCENDING)
        );
        var expected = List.of(
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.DESCENDING)
        );
        assertThat(actual.getOrderList()).isEqualTo(expected);
    }

    @Test
    void shouldHandleOneDuplication() {
        var actual = SortOrderList.of(
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING)
        );
        var expected = List.of(new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING));
        assertThat(actual.getOrderList()).isEqualTo(expected);
    }

    @Test
    void shouldHandleAllDuplications() {
        var actual = SortOrderList.of(
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.DESCENDING)
        );
        var expected = List.of(
                new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING),
                new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.DESCENDING),
                new SortOrder<>(TestTranslationEnum.THREE, SortOrder.Direction.DESCENDING)
                );
        assertThat(actual.getOrderList()).isEqualTo(expected);
    }
}