package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrder;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class WebSortAndPaginationMapper<F extends Enum<F> & FieldProvider> {
    private final F[] allEnumValues;
    private final SortOrder<F> defaultSortOrder;

    private static <F extends Enum<F> & FieldProvider> SortOrder<F> getDefaultField(F[] allEnumValues) {
        if (allEnumValues == null || allEnumValues.length == 0)
            throw new IllegalArgumentException("Empty Enum class is forbidden");
        return new SortOrder<>(allEnumValues[0], SortOrder.Direction.ASCENDING);
    }

    public WebSortAndPaginationMapper(Class<F> fieldEnum) {
        allEnumValues = fieldEnum.getEnumConstants();
        defaultSortOrder = getDefaultField(allEnumValues);
    }

    public SortAndPagination<F> map(Pageable pageable) {
        Stream<Sort.Order> validOrdersStream = pageable.getSort().stream().filter(this::isValidSortOrder);
        List<SortOrder<F>> mappedOrders = validOrdersStream.map(this::mapToDomainSortOrder).toList();
        List<SortOrder<F>> nonEmptyOrders = mappedOrders.isEmpty() ? List.of(defaultSortOrder) : mappedOrders;

        return new SortAndPagination<>(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                SortOrderList.of(nonEmptyOrders)
        );
    }

    private boolean isValidSortOrder(Sort.Order examinedSortOrder) {
        return Arrays.stream(allEnumValues).anyMatch(field -> field.getFieldName().equals(examinedSortOrder.getProperty()));
    }

    private SortOrder<F> mapToDomainSortOrder(Sort.Order order) {
        Optional<F> foundField = Arrays.stream(allEnumValues).filter(field -> field.getFieldName().equals(order.getProperty())).findFirst();
        return foundField
                .map(field -> new SortOrder<>(field, order.isAscending() ? SortOrder.Direction.ASCENDING : SortOrder.Direction.DESCENDING))
                .orElseThrow(() -> new IllegalArgumentException("no field with value: " + order.getProperty()));
    }
}
