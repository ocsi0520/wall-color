package com.my_wall_color.color_manager.user.jpa;

import com.my_wall_color.color_manager.user.User;
import com.my_wall_color.color_manager.user.UserFixture;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

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
    }

    @Test
    void toDomain() {
        var expectedUser = new UserFixture().jdoe;
        expectedUser.setId(971);

        JpaUser jpaEntity = new JpaUser(expectedUser.getId(), expectedUser.getUsername(), expectedUser.getPassword(), expectedUser.getName());
        User actual = unitUnderTest.toDomain(jpaEntity);
        assertThat(actual).isEqualTo(expectedUser);
    }
}