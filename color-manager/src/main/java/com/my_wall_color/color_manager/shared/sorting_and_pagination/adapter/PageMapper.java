package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {
  public <T> PageDTO<T> toDomain(Page<T> springPage) {
    return new PageDTO<>(
        springPage.getContent(),
        springPage.getNumber(),
        springPage.getSize(),
        springPage.getTotalElements(),
        springPage.getTotalPages()
    );
  }
}
