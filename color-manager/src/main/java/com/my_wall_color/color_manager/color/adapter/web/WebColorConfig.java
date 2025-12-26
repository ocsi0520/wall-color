package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web.WebSortAndPaginationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class WebColorConfig {
    @Bean
    WebSortAndPaginationMapper<ColorField> colorFieldWebSortAndPaginationMapper() {
        return new WebSortAndPaginationMapper<>(ColorField.class,
                (str) -> switch (str) {
                    case "id" -> Optional.of(ColorField.ID);
                    case "name" -> Optional.of(ColorField.NAME);
                    default -> Optional.empty();
                }
        );
    }
}
