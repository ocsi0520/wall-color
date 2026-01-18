package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import java.util.Objects;

public record SortOrder<FieldT extends Enum<FieldT>>(FieldT field,
                                                     Direction direction) {
  public enum Direction { ASCENDING, DESCENDING }

  // Only key is used for equality
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof SortOrder<?> other) {
      return Objects.equals(field, other.field);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(field);
  }
}
