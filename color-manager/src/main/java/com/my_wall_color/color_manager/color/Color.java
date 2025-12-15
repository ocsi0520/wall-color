package com.my_wall_color.color_manager.color;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Color {
    private Integer id;

    private Short red;
    private Short green;
    private Short blue;

    private String name;
    private Integer recordedBy;
}
