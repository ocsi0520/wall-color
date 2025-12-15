package com.my_wall_color.color_manager.user.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<JpaUser, Integer> {
    Optional<JpaUser> findByUsername(String username);
}
