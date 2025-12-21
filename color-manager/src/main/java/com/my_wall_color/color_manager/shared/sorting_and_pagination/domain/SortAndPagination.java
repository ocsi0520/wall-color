package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
@AllArgsConstructor
public class SortAndPagination<AFieldProvider extends FieldProvider> {
    private int pageSize;
    private int pageIndex;
    private LinkedHashMap<AFieldProvider, Boolean> sorting;
}
