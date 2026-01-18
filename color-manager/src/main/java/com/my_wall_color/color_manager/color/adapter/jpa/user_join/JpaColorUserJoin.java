package com.my_wall_color.color_manager.color.adapter.jpa.user_join;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "User_join_Color",
    uniqueConstraints = @UniqueConstraint(columnNames = {"app_user_id", "color_id"})
)
@AllArgsConstructor
@NoArgsConstructor
public class JpaColorUserJoin {
  @EmbeddedId
  private ColorUserJoinKey id;
}
