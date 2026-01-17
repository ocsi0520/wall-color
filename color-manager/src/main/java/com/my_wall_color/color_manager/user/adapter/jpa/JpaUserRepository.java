package com.my_wall_color.color_manager.user.adapter.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<JpaUser, Integer> {
  Optional<JpaUser> findByUsername(String username);
}
