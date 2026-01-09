package com.my_wall_color.color_manager.color.adapter.jpa;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.color.adapter.jpa.user_join.ColorUserJoinKey;
import com.my_wall_color.color_manager.color.adapter.jpa.user_join.JpaColorUserJoin;
import com.my_wall_color.color_manager.color.adapter.jpa.user_join.JpaColorUserJoinRepository;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.PageMapper;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.jpa.JpaSortAndPaginationMapper;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaColorRepositoryAdapter implements ColorRepository {
    private final JpaColorRepository implementation;
    private final JpaColorUserJoinRepository joinRepository;
    private final JpaColorMapper colorMapper;
    private final JpaSortAndPaginationMapper<ColorField> sapMapper;
    private final PageMapper pageMapper;

    @Override
    public Optional<Color> findById(Integer id) {
        return implementation.findById(id).map(colorMapper::toDomain);
    }

    @Override
    public List<Color> findAllAssociatedWith(Integer userId) {
        return joinRepository.findAllAssociatedColorForUser(userId).stream().map(colorMapper::toDomain).toList();
    }

    @Override
    public Color save(Color color) {
        var savableJpaColor = colorMapper.fromDomain(color);
        var savedJpaColor = implementation.save(savableJpaColor);
        var savedColor = colorMapper.toDomain(savedJpaColor);
        return savedColor;
    }

    @Override
    public void assignToUser(Color color, Integer userId) {
        joinRepository.save(new JpaColorUserJoin(new ColorUserJoinKey(userId, color.getId())));
    }

    @Override
    public PageDTO<Color> findAll(SortAndPagination<ColorField> sortAndPagination) {
        Pageable mappedSap = sapMapper.map(sortAndPagination);
        Page<JpaColor> jpaColorPage = implementation.findAll(mappedSap);
        Page<Color> mappedContentPage = jpaColorPage.map(colorMapper::toDomain);
        return pageMapper.toDomain(mappedContentPage);
    }

    // TODO: test from here
    @Override
    public void removeBy(Integer colorId) {
        joinRepository.deleteAllAssignmentsFor(colorId);
        implementation.deleteById(colorId);
    }

    @Override
    public void removeAssignment(Color color, Integer userId) {
        joinRepository.deleteById(new ColorUserJoinKey(userId, color.getId()));
    }

}
