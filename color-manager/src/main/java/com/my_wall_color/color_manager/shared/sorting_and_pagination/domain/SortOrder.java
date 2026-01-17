package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import java.util.Objects;

public record SortOrder<EnumValue extends Enum<EnumValue>>(EnumValue fieldProvider,
                                                           Direction direction) {
  public enum Direction { ASCENDING, DESCENDING; }

  // Only key is used for equality
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof SortOrder<?> other) {
      return Objects.equals(fieldProvider, other.fieldProvider);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldProvider);
  }
}
