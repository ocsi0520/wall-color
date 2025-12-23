package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpaSortAndPaginationMapper<F extends Enum<F> & FieldProvider> {
    private final EnumToColumnMapper<F> enumToColumnMapper;

    public JpaSortAndPaginationMapper(EnumToColumnMapper<F> enumToColumnMapper) {
        this.enumToColumnMapper = enumToColumnMapper;
    }
    public Pageable map(SortAndPagination<F> sap) {
        return PageRequest.of(sap.getPageIndex(), sap.getPageSize(), this.mapSort(sap.getSorting()));
    }

    private Sort mapSort(LinkedHashMap<F, Boolean> sorting) {
        List<Sort.Order> allOrders = sorting.entrySet().stream()
                .map(this::mapSortEntry)
                .collect(Collectors.toList());
        return Sort.by(allOrders);
    }

    private Sort.Order mapSortEntry(Map.Entry<F, Boolean> sortEntry) {
        String jpaFieldName = enumToColumnMapper.mapEnumToJpaColumn(sortEntry.getKey());
        Sort.Direction sortDirection = sortEntry.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort.Order(sortDirection, jpaFieldName);
    }
}
