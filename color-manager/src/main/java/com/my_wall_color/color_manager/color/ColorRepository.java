package com.my_wall_color.color_manager.color;

import java.util.List;
import java.util.Optional;

public interface ColorRepository {
    Optional<Color> findById(Integer id);
    List<Color> findAllAssociatedWith(Integer userId);
}
