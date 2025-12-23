package com.my_wall_color.color_manager.shared.sorting_and_pagination.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
}