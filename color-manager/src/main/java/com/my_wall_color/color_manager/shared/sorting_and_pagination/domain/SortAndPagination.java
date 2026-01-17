package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortAndPagination<FieldT extends Enum<FieldT>> {
  private int pageSize;
  private int pageIndex;
  private SortOrderList<FieldT> sorting;
}
