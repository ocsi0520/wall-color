package com.my_wall_color.color_manager.color.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
    @Test
    void shouldNotAcceptInvalidColorComponent() {
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, (short) 255, (short) 255, Short.MIN_VALUE, "min blue", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, (short) 255, (short) 256, (short) 0, "green above 1", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, Short.MAX_VALUE, (short) 255, (short) 0, "max red", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, (short) -1, (short) 255, (short) 0, "red below 1", 1));
    }

    @Test
    void shouldNotAcceptEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, (short) 255, (short) 255, (short) 0, "", 1));
        assertThrows(IllegalArgumentException.class, () -> Color.create(null, (short) 255, (short) 255, (short) 0, null, 1));
    }

    @Test
    void shouldCreateColor() {
        var color = Color.create(null, (short) 200, (short) 100, (short) 50, "random color", 5);
        assertThat(color.getId()).isNull();
        assertThat(color.getRed()).isEqualTo((short) 200);
        assertThat(color.getGreen()).isEqualTo((short) 100);
        assertThat(color.getBlue()).isEqualTo((short) 50);
        assertThat(color.getName()).isEqualTo("random color");
        assertThat(color.getRecordedBy()).isEqualTo(5);
    }
}