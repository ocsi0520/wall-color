package com.my_wall_color.color_manager.color.adapter.web;

public class ColorAlreadyAssignedException extends Exception {
    public ColorAlreadyAssignedException(int colorId, String username) {
        super("Color " + colorId + " is already assigned to user " + username);
    }
}
