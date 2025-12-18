package com.my_wall_color.color_manager.color.adapter.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Entity
@Table(name = "Color")
@Data
@NoArgsConstructor
public class JpaColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Short red;
    private Short green;
    private Short blue;

    private String name;
    private Integer recordedBy;
}
