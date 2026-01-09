package com.my_wall_color.color_manager.color.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
    @Test
    void shouldNotAcceptInvalidColorComponent() {
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, 255, 255, Short.MIN_VALUE, "min blue", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, 255, 256, 0, "green above 1", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, Short.MAX_VALUE, 255, 0, "max red", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, -1, 255, 0, "red below 1", 1));
    }

    @Test
    void shouldNotAcceptEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, 255, 255, 0, "", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, 255, 255, 0, null, 1));
    }

    @Test
    void shouldCreateColor() {
        var color = Color.create(null, 200, 100, 50, "random color", 5);
        assertThat(color.getId()).isNull();
        assertThat(color.getRed()).isEqualTo((short) 200);
        assertThat(color.getGreen()).isEqualTo((short) 100);
        assertThat(color.getBlue()).isEqualTo((short) 50);
        assertThat(color.getName()).isEqualTo("random color");
        assertThat(color.getRecordedBy()).isEqualTo(5);
    }
}