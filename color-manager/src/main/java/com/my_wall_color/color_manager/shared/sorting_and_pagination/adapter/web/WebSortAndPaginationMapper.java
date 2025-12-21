package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.FieldProvider;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.nimbusds.jose.util.Pair;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class WebSortAndPaginationMapper<F extends Enum<F> & FieldProvider> {
    private final Class<F> fieldEnum;

    private boolean validate(Sort.Order examinedSortOrder) {
        // TODO: check with functional interface so that we can get rid of FieldProvider interface
        return Arrays.stream(fieldEnum.getEnumConstants()).anyMatch(field -> field.getFieldName().equals(examinedSortOrder.getProperty()));
    }

    // TODO: refactor
    public SortAndPagination<F> map(Pageable pageable) {
        Stream<Sort.Order> orders = pageable.getSort().stream().filter(this::validate);

        LinkedHashMap<F, Boolean> mappedSorting = orders.map(order -> {
                    Optional<F> foundField = Arrays.stream(fieldEnum.getEnumConstants()).filter(field -> field.getFieldName().equals(order.getProperty())).findFirst();
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


        return new SortAndPagination<>(
                pageable.getPageSize(),
                pageable.getPageNumber(),
                mappedSorting
        );
    }
}
