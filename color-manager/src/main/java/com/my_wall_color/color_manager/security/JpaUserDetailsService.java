package com.my_wall_color.color_manager.security;

import com.my_wall_color.color_manager.adapter.JpaUser;
import com.my_wall_color.color_manager.adapter.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaUserDetailsService implements UserDetailsService {
    @Autowired
    private JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<JpaUser> result = userRepository.findByUsername(username);
        if (result.isEmpty()) throw new UsernameNotFoundException("Could not find user");

        JpaUser foundUser = result.get();
        return User.withUsername(foundUser.getUsername()).password(foundUser.getPassword()).build();
    }
}