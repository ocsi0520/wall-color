package com.my_wall_color.color_manager.user.jpa;

import com.my_wall_color.color_manager.user.User;
import com.my_wall_color.color_manager.user.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository implementation;
    private final JpaUserMapper mapper;

    public JpaUserRepositoryAdapter(JpaUserRepository implementation, JpaUserMapper mapper) {
        this.implementation = implementation;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return implementation.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return implementation.findById(id).map(mapper::toDomain);
    }
}
