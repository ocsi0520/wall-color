package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class WebSortAndPaginationMapper<F extends Enum<F>> {
    private final SortOrder<F> defaultSortOrder;
    private final SortOrderToEnumMapper<F> mapper;

    public SortAndPagination<F> map(Pageable pageable) {
        var filteredMappedSortOrder =
                pageable.getSort().stream()
                        .map(this::mapToDomainSortOrder)
                        .filter(Optional::isPresent)
                        .map(Optional::get).toList();
        List<SortOrder<F>> nonEmptyOrders = filteredMappedSortOrder.isEmpty() ? List.of(defaultSortOrder) : filteredMappedSortOrder;

        return new SortAndPagination<>(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                SortOrderList.of(nonEmptyOrders)
        );
    }

    private Optional<SortOrder<F>> mapToDomainSortOrder(Sort.Order order) {
        Optional<F> enumValue = this.mapper.mapSortOrderToEnum(order.getProperty());
        SortOrder.Direction direction = order.isAscending() ? SortOrder.Direction.ASCENDING : SortOrder.Direction.DESCENDING;
        return enumValue.map(f -> new SortOrder<>(f, direction));
    }
}
