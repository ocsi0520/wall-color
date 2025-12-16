package com.my_wall_color.color_manager.user.jpa;

import com.my_wall_color.color_manager.user.User;
import com.my_wall_color.test_utils.PostgresContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaUserRepositoryAdapterTest extends PostgresContainerTest {
    @Autowired
    JpaUserRepositoryAdapter adapter;

    // TODO: test fixture
    private final String password = "$2a$12$/wnuwqCoou1NwfDGzAPTFOsDgbyIblbOyGp.8WRvPMYt/GWSn8XYy";
    User jdoeUser = new User(1, "jdoe", password, "John Doe");

    @Test
    void shouldReturnJdoeUserById() {
        Optional<User> user = adapter.findById(1);
        assertThat(user).contains(jdoeUser);
    }

    @Test
    void shouldReturnEmptyById() {
        Optional<User> user = adapter.findById(9999);
        assertThat(user).isEmpty();
    }

    @Test
    void shouldReturnJdoeUserByName() {
        Optional<User> user = adapter.findByUsername("jdoe");
        assertThat(user).contains(jdoeUser);
    }

    @Test
    void shouldReturnEmptyUserByName() {
        Optional<User> user = adapter.findByUsername("non-existent-user");
        assertThat(user).isEmpty();
    }
}
