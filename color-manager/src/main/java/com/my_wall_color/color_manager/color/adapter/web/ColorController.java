package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web.WebSortAndPaginationMapper;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
public class ColorController {
    private final ColorRepository colorRepository;
    private final WebSortAndPaginationMapper<ColorField> sortAndPaginationMapper;
    @Value("${spring.data.web.pageable.max-page-index}")
    private Integer maxPageIndex;

    @GetMapping("/{id}")
    private ResponseEntity<Color> getColorById(@PathVariable int id) {
        return ResponseEntity.of(colorRepository.findById(id));
    }

    @GetMapping
    private ResponseEntity<PageDTO<Color>> getAllColors(Pageable pageable) {
        var normalizedPageRequest = PageRequest.of(
                Math.min(pageable.getPageNumber(), maxPageIndex),
                pageable.getPageSize(),
                pageable.getSort()
        );
        SortAndPagination<ColorField> SortAndPaginationInfo = sortAndPaginationMapper.map(normalizedPageRequest);
        PageDTO<Color> page = colorRepository.findAll(SortAndPaginationInfo);
        return ResponseEntity.ok(page);
    }
}
