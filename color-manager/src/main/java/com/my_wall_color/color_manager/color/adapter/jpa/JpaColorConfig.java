package com.my_wall_color.color_manager.color.adapter.jpa;

import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa.JpaSortAndPaginationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaColorConfig {
    @Bean
    JpaSortAndPaginationMapper<ColorField> JpaSortAndPaginationMapper() {
        return new JpaSortAndPaginationMapper<>(value ->
                switch (value) {
                    case NAME -> "name";
                    case ID -> "id";
                }
        );
    }
}
