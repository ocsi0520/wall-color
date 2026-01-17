package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

@FunctionalInterface
public interface EnumToColumnMapper<F extends Enum<F>> {
  String mapEnumToJpaColumn(F fieldEnum);
}
