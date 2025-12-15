package com.my_wall_color.color_manager.color.jpa;

import com.my_wall_color.color_manager.color.Color;
import com.my_wall_color.color_manager.color.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaColorRepositoryAdapter implements ColorRepository {
    private final JpaColorRepository implementation;
    private final JpaColorMapper mapper;

    @Override
    public Optional<Color> findById(Integer id) {
        return implementation.findById(id).map(mapper::toDomain);
    }
}
