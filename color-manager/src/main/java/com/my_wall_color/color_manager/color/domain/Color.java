package com.my_wall_color.color_manager.color.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Color {
    private Integer id;

    private Short red;
    private Short green;
    private Short blue;

    private String name;
    private Integer recordedBy;

    public static Color create(Integer id, int red, int green, int blue, String name, Integer recordedBy) throws IllegalArgumentException {
        return create(id, (short) red, (short) green, (short) blue, name, recordedBy);
    }

    public static Color create(Integer id, short red, short green, short blue, String name, Integer recordedBy) throws IllegalArgumentException {
        boolean hasIncorrectComponent = Stream.of(red, green, blue).anyMatch(component -> component < 0 || component > 255);
        if (hasIncorrectComponent) throw new IllegalArgumentException("all color component must be between 0 and 255");
        boolean hasInvalidName = name == null || name.isEmpty();
        if (hasInvalidName) throw new IllegalArgumentException("color must be named");
        return new Color(id, red, green, blue, name, recordedBy);
    }
}
