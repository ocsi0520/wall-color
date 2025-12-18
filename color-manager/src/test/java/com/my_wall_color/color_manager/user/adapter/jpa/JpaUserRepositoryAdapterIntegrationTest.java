package com.my_wall_color.color_manager.user.adapter.jpa;

import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaUserRepositoryAdapterIntegrationTest extends IntegrationTest {
    @Autowired
    JpaUserRepositoryAdapter adapter;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
    }

    @Test
    void shouldReturnJdoeUserById() {
        Optional<User> user = adapter.findById(userFixture.jdoe.getId());
        assertThat(user).contains(userFixture.jdoe);
    }

    @Test
    void shouldReturnEmptyById() {
        Optional<User> user = adapter.findById(userFixture.nonExistent.getId());
        assertThat(user).isEmpty();
    }

    @Test
    void shouldReturnJdoeUserByName() {
        Optional<User> user = adapter.findByUsername(userFixture.jdoe.getUsername());
        assertThat(user).contains(userFixture.jdoe);
    }

    @Test
    void shouldReturnEmptyUserByName() {
        Optional<User> user = adapter.findByUsername(userFixture.nonExistent.getUsername());
        assertThat(user).isEmpty();
    }
}
