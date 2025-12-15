package com.my_wall_color.color_manager.user.jpa;

import com.my_wall_color.color_manager.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JpaUserMapper {
    JpaUser fromDomain(User user);
    User toDomain(JpaUser jpaUser);
}
