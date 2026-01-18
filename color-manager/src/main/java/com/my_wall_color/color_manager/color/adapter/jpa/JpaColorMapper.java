package com.my_wall_color.color_manager.color.adapter.jpa;

import com.my_wall_color.color_manager.color.domain.Color;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(componentModel = "spring")
public interface JpaColorMapper {
  JpaColor fromDomain(Color color);

  @ObjectFactory
  default Color toDomain(JpaColor color) {
    if (color == null) {
      return null;
    }

    return Color.create(
        color.getId(),
        color.getRed(),
        color.getGreen(),
        color.getBlue(),
        color.getName(),
        color.getRecordedBy()
    );
  }
}
