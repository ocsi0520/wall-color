package com.my_wall_color.color_manager.color.domain;

public record ColorCreationRequest(Short red, Short green, Short blue, String name) {
    public static ColorCreationRequest of(int red, int green, int blue, String name) {
        return new ColorCreationRequest((short) red, (short) green, (short) blue, name);
    }
}
