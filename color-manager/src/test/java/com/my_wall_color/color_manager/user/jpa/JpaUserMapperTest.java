package com.my_wall_color.color_manager.user.jpa;

import com.my_wall_color.color_manager.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class JpaUserMapperTest {
    JpaUserMapper unitUnderTest = Mappers.getMapper(JpaUserMapper.class);

    private final String password = "$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy";

    @Test
    void fromDomain() {
        User domain = new User(971, "jdoe", password, "John Doe");
        JpaUser actual = unitUnderTest.fromDomain(domain);
        assertThat(actual.getId()).isEqualTo(971);
        assertThat(actual.getPassword()).isEqualTo(password);
        assertThat(actual.getName()).isEqualTo("John Doe");
    }

    @Test
    void toDomain() {
        JpaUser jpaEntity = new JpaUser(971, "jdoe", password, "John Doe");
        User actual = unitUnderTest.toDomain(jpaEntity);
        assertThat(actual.getId()).isEqualTo(971);
        assertThat(actual.getPassword()).isEqualTo(password);
        assertThat(actual.getName()).isEqualTo("John Doe");
    }
}