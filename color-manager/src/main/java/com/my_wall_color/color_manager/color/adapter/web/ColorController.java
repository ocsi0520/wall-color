package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web.WebSortAndPaginationMapper;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/color")
@AllArgsConstructor
public class ColorController {
    private ColorRepository colorRepository;
    private WebSortAndPaginationMapper<ColorField> sortAndPaginationMapper;

    @GetMapping("/{id}")
    private ResponseEntity<Color> getColorById(@PathVariable int id) {
        return ResponseEntity.of(colorRepository.findById(id));
    }

    @GetMapping
    private ResponseEntity<PageDTO<Color>> getAllColors(Pageable pageable) {
        var pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                // TODO: get rid of this and create default sorting in WebSortAndPaginationMapper
                pageable.getSortOr(Sort.by(Sort.Order.asc("id")))
        );
        SortAndPagination<ColorField> SortAndPaginationInfo = sortAndPaginationMapper.map(pageRequest);
        PageDTO<Color> page = colorRepository.findAll(SortAndPaginationInfo);
        return ResponseEntity.ok(page);
    }
}
