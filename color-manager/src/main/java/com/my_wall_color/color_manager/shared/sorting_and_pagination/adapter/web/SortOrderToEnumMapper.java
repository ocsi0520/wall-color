package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import java.util.Optional;

@FunctionalInterface
public interface SortOrderToEnumMapper<F extends Enum<F>> {
    Optional<F> mapSortOrderToEnum(String sortOrderName);
}
