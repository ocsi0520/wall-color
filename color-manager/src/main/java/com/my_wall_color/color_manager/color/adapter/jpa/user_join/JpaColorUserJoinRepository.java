package com.my_wall_color.color_manager.color.adapter.jpa.user_join;

import com.my_wall_color.color_manager.color.adapter.jpa.JpaColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaColorUserJoinRepository extends JpaRepository<JpaColorUserJoin, ColorUserJoinKey> {
    @Query("""
            SELECT c FROM
            JpaColor c INNER JOIN JpaColorUserJoin cuj ON cuj.id.colorId = c.id
            WHERE cuj.id.userId = :userId""")
    List<JpaColor> findAllAssociatedColorForUser(@Param("userId") Integer userId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM JpaColorUserJoin WHERE id.colorId = :colorId")
    int deleteAllAssignmentsFor(@Param("colorId") Integer colorId);
}
