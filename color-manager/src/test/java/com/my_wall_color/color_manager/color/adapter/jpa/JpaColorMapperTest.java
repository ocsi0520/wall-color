package com.my_wall_color.color_manager.color.adapter.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_wall_color.color_manager.color.domain.Color;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class JpaColorMapperTest {
  JpaColorMapper unitUnderTest = Mappers.getMapper(JpaColorMapper.class);

  @Test
  void fromDomain() {
    Color color = Color.create(971, 10, 20, 30, "test-color", 31);
    JpaColor jpaColor = unitUnderTest.fromDomain(color);
    assertThat(jpaColor.getId()).isEqualTo(971);
    assertThat(jpaColor.getRed()).isEqualTo((short) 10);
    assertThat(jpaColor.getGreen()).isEqualTo((short) 20);
    assertThat(jpaColor.getBlue()).isEqualTo((short) 30);
    assertThat(jpaColor.getName()).isEqualTo("test-color");
    assertThat(jpaColor.getRecordedBy()).isEqualTo(31);
  }

  @Test
  void fromNullDomain() {
    assertThat(unitUnderTest.fromDomain(null)).isNull();
  }

  @Test
  void toDomain() {
    JpaColor jpaColor = new JpaColor(971, (short) 10, (short) 20, (short) 30, "test-color", 31);
    Color color = unitUnderTest.toDomain(jpaColor);
    assertThat(color.getId()).isEqualTo(971);
    assertThat(color.getRed()).isEqualTo((short) 10);
    assertThat(color.getGreen()).isEqualTo((short) 20);
    assertThat(color.getBlue()).isEqualTo((short) 30);
    assertThat(color.getName()).isEqualTo("test-color");
    assertThat(color.getRecordedBy()).isEqualTo(31);
  }

  @Test
  void toNullDomain() {
    assertThat(unitUnderTest.toDomain(null)).isNull();
  }
}