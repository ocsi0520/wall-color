package com.my_wall_color.color_manager.color;

import java.util.Optional;

public interface ColorRepository {
    Optional<Color> findById(Integer id);
}
