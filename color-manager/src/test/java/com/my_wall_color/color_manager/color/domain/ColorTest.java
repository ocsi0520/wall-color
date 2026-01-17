package com.my_wall_color.color_manager.color.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ColorTest {
  @ParameterizedTest
  @MethodSource("invalidColorComponents")
  void shouldNotAcceptInvalidColorComponent(
      int red, int green, int blue, String name) {

    assertThrows(
        IllegalArgumentException.class,
        () -> Color.create(null, red, green, blue, name, 1)
    );
  }

  static Stream<Arguments> invalidColorComponents() {
    return Stream.of(
        Arguments.of(255, 255, Short.MIN_VALUE, "min blue"),
        Arguments.of(255, 256, 0, "green above 255"),
        Arguments.of(Short.MAX_VALUE, 255, 0, "max red"),
        Arguments.of(-1, 255, 0, "red below 0")
    );
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldNotAcceptEmptyName(String name) {
    assertThrows(
        IllegalArgumentException.class,
        () -> Color.create(null, 255, 255, 0, name, 1)
    );
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