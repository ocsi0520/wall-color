package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.user.domain.User;
import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public class UserDetailsMapper {
  UserDetails fromDomain(User user) {
    if (user == null) {
      return null;
    }

    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: extract roles into separate file (enum or static string members)
        if (user.getIsAdmin()) {
          return List.of(new SimpleGrantedAuthority("ADMIN"));
        } else {
          return List.of();
        }
      }

      @Override
      public String getPassword() {
        return user.getPassword();
      }

      @Override
      public String getUsername() {
        return user.getUsername();
      }
    };
  }
}
