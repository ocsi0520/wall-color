package com.my_wall_color.color_manager.color.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface JpaColorRepository extends JpaRepository<JpaColor, Integer> {
    // TODO: proper JPA equivalent solution rather than SQL query
    //  check https://stackoverflow.com/a/22822283
    @Query(
            value = "SELECT * FROM Color as c INNER JOIN User_join_Color as j ON c.id = j.color_id WHERE j.app_user_id = ?1",
            nativeQuery = true
    )
    List<JpaColor> findAllAssociatedColorForUser(Integer userId);
}
