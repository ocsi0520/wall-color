package com.my_wall_color.color_manager.user.domain;

import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserRepository {
  Optional<User> findByUsername(String username);

  Optional<User> findById(Integer id);

  User save(User user);

  default User requiredById(Integer id) throws NoSuchElementException {
    return findById(id).orElseThrow(
        () -> new NoSuchElementException("No user was found by id " + id));
  }

  default User requiredByUsername(String username) throws NoSuchElementException {
    return findByUsername(username).orElseThrow(
        () -> new NoSuchElementException("No user was found with username " + username));
  }
}
