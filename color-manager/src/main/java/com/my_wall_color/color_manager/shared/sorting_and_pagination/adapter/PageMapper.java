package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageMapper {
  public <T> PageDto<T> toDomain(Page<T> springPage) {
    return new PageDto<>(
        springPage.getContent(),
        springPage.getNumber(),
        springPage.getSize(),
        springPage.getTotalElements(),
        springPage.getTotalPages()
    );
  }
}
