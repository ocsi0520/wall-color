package com.my_wall_color.color_manager.security;

import static org.assertj.core.api.Assertions.assertThat;

import com.my_wall_color.color_manager.user.UserFixture;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

class UserDetailsMapperTest {
  UserDetailsMapper unitUnderTest = Mappers.getMapper(UserDetailsMapper.class);

  @Test
  void shouldMapAdminUser() {
    var jdoeUser = new UserFixture().jdoe;

    UserDetails details = unitUnderTest.fromDomain(jdoeUser);
    assertThat(details.isAccountNonExpired()).isTrue();
    assertThat(details.isCredentialsNonExpired()).isTrue();
    assertThat(details.isAccountNonLocked()).isTrue();
    assertThat(details.isEnabled()).isTrue();
    assertThat(details.getAuthorities()).isEqualTo(List.of(new SimpleGrantedAuthority("ADMIN")));
    assertThat(details.getUsername()).isEqualTo(jdoeUser.getUsername());
    assertThat(details.getPassword()).isEqualTo(jdoeUser.getPassword());
  }

  @Test
  void shouldMapSimpleUser() {
    var alexUser = new UserFixture().alex;

    UserDetails details = unitUnderTest.fromDomain(alexUser);
    assertThat(details.isAccountNonExpired()).isTrue();
    assertThat(details.isCredentialsNonExpired()).isTrue();
    assertThat(details.isAccountNonLocked()).isTrue();
    assertThat(details.isEnabled()).isTrue();
    assertThat(details.getAuthorities()).hasSize(0);
    assertThat(details.getUsername()).isEqualTo(alexUser.getUsername());
    assertThat(details.getPassword()).isEqualTo(alexUser.getPassword());
  }

  @Test
  void shouldMapNull() {
    assertThat(unitUnderTest.fromDomain(null)).isNull();
  }
}