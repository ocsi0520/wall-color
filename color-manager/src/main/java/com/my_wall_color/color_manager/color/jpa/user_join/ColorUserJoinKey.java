package com.my_wall_color.color_manager.color.jpa.user_join;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ColorUserJoinKey implements Serializable {
    @Column(name = "app_user_id")
    private Integer userId;

    @Column(name = "color_id")
    private Integer colorId;
}
