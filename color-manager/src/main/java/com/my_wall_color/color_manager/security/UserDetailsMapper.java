package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.user.User;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public class UserDetailsMapper {
    UserDetails fromDomain(User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
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
