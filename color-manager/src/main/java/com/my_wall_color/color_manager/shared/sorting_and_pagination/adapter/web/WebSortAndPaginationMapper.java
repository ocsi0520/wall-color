package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.nimbusds.jose.util.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebSortAndPaginationMapper<F extends Enum<F> & FieldProvider> {
    private final F[] allEnumValues;
    private final F defaultField;

    private static <F extends Enum<F> & FieldProvider> F getDefaultField(F[] allEnumValues) {
        if (allEnumValues == null || allEnumValues.length == 0)
            throw new IllegalArgumentException("Empty Enum class is forbidden");
        return allEnumValues[0];
    }

    public WebSortAndPaginationMapper(Class<F> fieldEnum) {
        allEnumValues = fieldEnum.getEnumConstants();
        defaultField = getDefaultField(allEnumValues);
    }

    private boolean isValidSortOrder(Sort.Order examinedSortOrder) {
        return Arrays.stream(allEnumValues).anyMatch(field -> field.getFieldName().equals(examinedSortOrder.getProperty()));
    }

    // TODO: refactor
    public SortAndPagination<F> map(Pageable pageable) {
        Stream<Sort.Order> orders = pageable.getSort().stream().filter(this::isValidSortOrder);

        LinkedHashMap<F, Boolean> mappedSorting = orders.map(order -> {
                    Optional<F> foundField = Arrays.stream(allEnumValues).filter(field -> field.getFieldName().equals(order.getProperty())).findFirst();
                    return foundField
                            .map(field -> Pair.of(field, order.isAscending()))
                            .orElseThrow(() -> new IllegalArgumentException("no field with value: " + order.getProperty()));
                })
                .collect(
                        Collectors.toMap(
                                Pair::getLeft,
                                Pair::getRight,
                                (oldValue, newValue) -> newValue,
                                LinkedHashMap::new
                        ));

        if (mappedSorting.isEmpty())
            mappedSorting.put(defaultField, true);
        
        return new SortAndPagination<>(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                mappedSorting
        );
    }
}
