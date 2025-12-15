package com.my_wall_color.color_manager.color.jpa;

import com.my_wall_color.color_manager.color.Color;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaColorMapper {
    JpaColor fromDomain(Color color);
    Color toDomain(JpaColor color);
}
