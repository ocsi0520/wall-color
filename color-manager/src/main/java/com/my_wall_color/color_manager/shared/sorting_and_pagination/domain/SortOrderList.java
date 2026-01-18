package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class SortOrderList<FieldT extends Enum<FieldT>> {
  private final LinkedHashSet<SortOrder<FieldT>> orderedList = new LinkedHashSet<>();

  public static <FieldT extends Enum<FieldT>> SortOrderList<FieldT> of(
      Collection<SortOrder<FieldT>> sortOrders) {
    return new SortOrderList<>(sortOrders);
  }

  @SafeVarargs
  public static <FieldT extends Enum<FieldT>> SortOrderList<FieldT> of(
      SortOrder<FieldT>... sortOrders) {
    return of(Arrays.stream(sortOrders).toList());
  }

  private SortOrderList(Collection<SortOrder<FieldT>> sortOrders) {
    orderedList.addAll(sortOrders);
  }

  public final List<SortOrder<FieldT>> getOrderList() {
    return orderedList.stream().toList();
  }
}
