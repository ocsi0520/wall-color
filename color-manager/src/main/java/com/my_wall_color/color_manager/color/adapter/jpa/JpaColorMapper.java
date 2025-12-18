package com.my_wall_color.color_manager.color.adapter.jpa;

import com.my_wall_color.color_manager.color.domain.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaColorMapper {
    JpaColor fromDomain(Color color);
    Color toDomain(JpaColor color);
}
