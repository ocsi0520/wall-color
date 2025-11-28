package com.my_wall_color.color_manager.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<JpaUser, Long> {
    Optional<JpaUser> findByUsername(String username);
}
