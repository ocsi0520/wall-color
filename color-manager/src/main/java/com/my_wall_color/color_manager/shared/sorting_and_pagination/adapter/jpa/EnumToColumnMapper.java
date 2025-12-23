package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;

@FunctionalInterface
public interface EnumToColumnMapper<F extends Enum<F> & FieldProvider> {
    String mapEnumToJpaColumn(F fieldEnum);

    static <F extends Enum<F> & FieldProvider> EnumToColumnMapper<F> getEnumValueMapper() {
        return FieldProvider::getFieldName;
    }
}
