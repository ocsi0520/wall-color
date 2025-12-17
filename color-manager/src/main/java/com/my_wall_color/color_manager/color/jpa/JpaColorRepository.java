package com.my_wall_color.color_manager.color.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaColorRepository extends JpaRepository<JpaColor, Integer> {
    @Query("""
            SELECT c FROM
            JpaColor c INNER JOIN JpaColorUserJoin cuj ON cuj.id.colorId = c.id
            WHERE cuj.id.userId = :userId""")
    List<JpaColor> findAllAssociatedColorForUser(@Param("userId") Integer userId);
}
