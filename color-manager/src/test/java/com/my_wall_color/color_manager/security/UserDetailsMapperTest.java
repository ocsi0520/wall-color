package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsMapperTest {
    UserDetailsMapper unitUnderTest = Mappers.getMapper(UserDetailsMapper.class);

    private final String password = "$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy";

    @Test
    void fromDomain() {
        // TODO: de-duplicate user test data into Fixture classes
        User domain = new User(971, "jdoe", password, "John Doe");
        UserDetails details = unitUnderTest.fromDomain(domain);
        assertThat(details.isAccountNonExpired()).isTrue();
        assertThat(details.isCredentialsNonExpired()).isTrue();
        assertThat(details.isAccountNonLocked()).isTrue();
        assertThat(details.isEnabled()).isTrue();
        assertThat(details.getPassword()).isEqualTo(password);
        assertThat(details.getAuthorities()).hasSize(0);
        assertThat(details.getUsername()).isEqualTo("jdoe");
    }
}