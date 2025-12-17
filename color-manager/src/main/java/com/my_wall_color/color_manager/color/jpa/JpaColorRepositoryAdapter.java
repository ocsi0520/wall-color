package com.my_wall_color.color_manager.color.jpa;

import com.my_wall_color.color_manager.color.Color;
import com.my_wall_color.color_manager.color.ColorRepository;
import com.my_wall_color.color_manager.color.jpa.user_join.ColorUserJoinKey;
import com.my_wall_color.color_manager.color.jpa.user_join.JpaColorUserJoin;
import com.my_wall_color.color_manager.color.jpa.user_join.JpaColorUserJoinRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaColorRepositoryAdapter implements ColorRepository {
    private final JpaColorRepository implementation;
    private final JpaColorUserJoinRepository joinRepository;
    private final JpaColorMapper mapper;

    @Override
    public Optional<Color> findById(Integer id) {
        return implementation.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Color> findAllAssociatedWith(Integer userId) {
        return implementation.findAllAssociatedColorForUser(userId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Color save(Color color) {
        var savableJpaColor = mapper.fromDomain(color);
        var savedJpaColor = implementation.save(savableJpaColor);
        var savedColor = mapper.toDomain(savedJpaColor);
        return savedColor;
    }

    @Override
    public void assignToUser(Color color, Integer userId) {
        joinRepository.save(new JpaColorUserJoin(new ColorUserJoinKey(userId, color.getId())));
    }

}
