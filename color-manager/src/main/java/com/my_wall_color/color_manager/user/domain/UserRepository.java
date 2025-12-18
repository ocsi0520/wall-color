package com.my_wall_color.color_manager.user.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Integer id);
    User save(User user);
}
