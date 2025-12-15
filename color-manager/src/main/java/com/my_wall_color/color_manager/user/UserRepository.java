package com.my_wall_color.color_manager.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
