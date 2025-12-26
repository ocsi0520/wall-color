package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public class SortOrderList<AFieldProvider extends Enum<AFieldProvider>> {
    private final LinkedHashSet<SortOrder<AFieldProvider>> orderedList = new LinkedHashSet<>();

    public static <AFieldProvider extends Enum<AFieldProvider>> SortOrderList<AFieldProvider>
    of(Collection<SortOrder<AFieldProvider>> sortOrders) {
        return new SortOrderList<>(sortOrders);
    }

    @SafeVarargs
    public static <AFieldProvider extends Enum<AFieldProvider>> SortOrderList<AFieldProvider>
    of(SortOrder<AFieldProvider> ...sortOrders) {
        return of(Arrays.stream(sortOrders).toList());
    }

    private SortOrderList(Collection<SortOrder<AFieldProvider>> sortOrders) {
        orderedList.addAll(sortOrders);
    }

    public final List<SortOrder<AFieldProvider>> getOrderList() {
        return orderedList.stream().toList();
    }
}
