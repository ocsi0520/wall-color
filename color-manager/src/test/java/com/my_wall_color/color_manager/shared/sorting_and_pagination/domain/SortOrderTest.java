package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_wall_color.color_manager.color.domain.ColorField;
import java.util.Date;
import org.junit.jupiter.api.Test;

class SortOrderTest {
  @Test
  void shouldSayEqualForSameSortOrder() {
    var ascendingOrder = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING);
    assertThat(ascendingOrder).isEqualTo(ascendingOrder);
  }

  @Test
  void shouldSayEqualForEqualSortOrder() {
    var ascendingOrder = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING);
    var descendingOrder = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.DESCENDING);
    assertThat(ascendingOrder).isEqualTo(descendingOrder);
  }

  @Test
  void shouldSayDifferentForDifferentKeyButSameDirection() {
    var order1 = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING);
    var order2 = new SortOrder<>(TestTranslationEnum.TWO, SortOrder.Direction.ASCENDING);
    assertThat(order1).isNotEqualTo(order2);
  }

  @Test
  void shouldSayDifferentForDifferentTypeOfEnum() {
    var order1 = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING);
    var order2 = new SortOrder<>(ColorField.ID, SortOrder.Direction.ASCENDING);
    assertThat(order1).isNotEqualTo(order2);
  }

  @Test
  void shouldSayDifferentForEntirelyDifferentType() {
    var order1 = new SortOrder<>(TestTranslationEnum.ONE, SortOrder.Direction.ASCENDING);
    Date d = new Date();
    assertThat(order1).isNotEqualTo(d);
  }
}