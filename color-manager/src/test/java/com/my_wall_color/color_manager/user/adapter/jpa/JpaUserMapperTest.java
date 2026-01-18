package com.my_wall_color.color_manager.user.adapter.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_wall_color.color_manager.user.UserFixture;
import com.my_wall_color.color_manager.user.domain.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class JpaUserMapperTest {
  JpaUserMapper unitUnderTest = Mappers.getMapper(JpaUserMapper.class);

  @Test
  void fromDomain() {
    var jdoeUser = new UserFixture().jdoe;
    jdoeUser.setId(971);
    JpaUser actual = unitUnderTest.fromDomain(jdoeUser);
    assertThat(actual.getId()).isEqualTo(971);
    assertThat(actual.getUsername()).isEqualTo(jdoeUser.getUsername());
    assertThat(actual.getPassword()).isEqualTo(jdoeUser.getPassword());
    assertThat(actual.getName()).isEqualTo(jdoeUser.getName());
    assertThat(actual.getIsAdmin()).isEqualTo(jdoeUser.getIsAdmin());
  }

  @Test
  void fromNullDomain() {
    assertThat(unitUnderTest.fromDomain(null)).isNull();
  }

  @Test
  void toDomain() {
    var expectedUser = new UserFixture().jdoe;
    expectedUser.setId(971);

    JpaUser jpaEntity =
        new JpaUser(expectedUser.getId(), expectedUser.getUsername(), expectedUser.getPassword(),
            expectedUser.getName(), expectedUser.getIsAdmin());
    User actual = unitUnderTest.toDomain(jpaEntity);
    assertThat(actual).isEqualTo(expectedUser);
  }

  @Test
  void toNullDomain() {
    assertThat(unitUnderTest.toDomain(null)).isNull();
  }
}