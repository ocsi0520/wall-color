package com.my_wall_color.color_manager.adapter;

import jakarta.persistence.*;

@Entity
@Table(name = "Color")
public class JpaColor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private short red;
    private short green;
    private short blue;

    private String name;
    private Integer recorded_by;

    public JpaColor(Integer id, short red, short green, short blue, String name, Integer recorded_by) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
        this.recorded_by = recorded_by;
    }

    public JpaColor() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getRed() {
        return red;
    }

    public void setRed(short red) {
        this.red = red;
    }

    public short getGreen() {
        return green;
    }

    public void setGreen(short green) {
        this.green = green;
    }

    public short getBlue() {
        return blue;
    }

    public void setBlue(short blue) {
        this.blue = blue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(Integer recorded_by) {
        this.recorded_by = recorded_by;
    }
}
