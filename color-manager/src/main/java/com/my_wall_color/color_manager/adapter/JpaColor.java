package com.my_wall_color.color_manager.adapter;

import jakarta.persistence.*;

@Entity
@Table(name = "Color")
public class JpaColor {
    @Id
    @GeneratedValue
    private long id;

    private short red;
    private short green;
    private short blue;

    private String name;
    private int recorded_by;

    public JpaColor(long id, short red, short green, short blue, String name, int recorded_by) {
        this.id = id;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.name = name;
        this.recorded_by = recorded_by;
    }

    public JpaColor() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getRecorded_by() {
        return recorded_by;
    }

    public void setRecorded_by(int recorded_by) {
        this.recorded_by = recorded_by;
    }
}
