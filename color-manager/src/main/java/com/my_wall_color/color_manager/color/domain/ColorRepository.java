package com.my_wall_color.color_manager.color.domain;

import java.util.List;
import java.util.Optional;

// TODO: check whether it's a better option to provide whole User object instead of a plain ID
public interface ColorRepository {
    Optional<Color> findById(Integer id);
    List<Color> findAllAssociatedWith(Integer userId);
    Color save(Color color);
    void assignToUser(Color color, Integer userId);
}
