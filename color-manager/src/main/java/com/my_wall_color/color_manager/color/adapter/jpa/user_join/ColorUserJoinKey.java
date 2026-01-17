package com.my_wall_color.color_manager.color.adapter.jpa.user_join;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ColorUserJoinKey implements Serializable {
  @Column(name = "app_user_id")
  private Integer userId;

  @Column(name = "color_id")
  private Integer colorId;
}
