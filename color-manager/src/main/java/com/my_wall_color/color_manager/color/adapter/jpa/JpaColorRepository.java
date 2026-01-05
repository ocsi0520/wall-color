package com.my_wall_color.color_manager.color.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JpaColorRepository extends
        JpaRepository<JpaColor, Integer>, PagingAndSortingRepository<JpaColor, Integer> {
}
