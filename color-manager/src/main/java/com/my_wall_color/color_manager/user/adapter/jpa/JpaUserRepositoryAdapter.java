package com.my_wall_color.color_manager.user.adapter.jpa;

import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.domain.UserRepository;
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

    @Override
    public User save(User user) {
        var savableJpaUser = mapper.fromDomain(user);
        var savedJpaUser = implementation.save(savableJpaUser);
        var savedUser = mapper.toDomain(savedJpaUser);
        return savedUser;
    }
}
