package com.my_wall_color.color_manager.color.jpa;

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

    private short red;
    private short green;
    private short blue;

    private String name;
    private Integer recordedBy;
}
