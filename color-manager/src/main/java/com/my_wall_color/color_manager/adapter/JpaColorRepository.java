package com.my_wall_color.color_manager.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaColorRepository extends JpaRepository<JpaColor, Long> {
}
