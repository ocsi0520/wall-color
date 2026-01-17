package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class JpaSortAndPaginationMapper<F extends Enum<F>> {
  private final EnumToColumnMapper<F> enumToColumnMapper;

  public JpaSortAndPaginationMapper(EnumToColumnMapper<F> enumToColumnMapper) {
    this.enumToColumnMapper = enumToColumnMapper;
  }

  public Pageable map(SortAndPagination<F> sap) {
    return PageRequest.of(sap.getPageIndex(), sap.getPageSize(), this.mapSort(sap.getSorting()));
  }

  private Sort mapSort(SortOrderList<F> sorting) {
    List<Sort.Order> allOrders = sorting.getOrderList().stream()
        .map(this::mapSortOrderFromDomain)
        .collect(Collectors.toList());
    return Sort.by(allOrders);
  }

  private Sort.Direction mapSortDirection(SortOrder.Direction direction) {
    return direction.equals(SortOrder.Direction.ASCENDING) ? Sort.Direction.ASC :
        Sort.Direction.DESC;
  }

  private Sort.Order mapSortOrderFromDomain(SortOrder<F> sortOrder) {
    String jpaFieldName = enumToColumnMapper.mapEnumToJpaColumn(sortOrder.fieldProvider());
    Sort.Direction sortDirection = mapSortDirection(sortOrder.direction());
    return new Sort.Order(sortDirection, jpaFieldName);
  }
}
