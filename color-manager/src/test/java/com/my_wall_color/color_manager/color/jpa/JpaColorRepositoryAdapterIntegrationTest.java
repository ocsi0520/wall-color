package com.my_wall_color.color_manager.color.jpa;


import com.my_wall_color.color_manager.color.Color;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaColorRepositoryAdapterIntegrationTest extends IntegrationTest {
    @Autowired
    private JpaColorRepositoryAdapter unitUnderTest;

    @BeforeEach
    void injectFixtures() {
        userFixture.injectAll();
        colorFixture.injectAll(userFixture);
    }

    @Test
    void shouldReturnSulyom() {
        assertThat(unitUnderTest.findById(colorFixture.sulyom.getId())).contains(colorFixture.sulyom);
    }

    @Test
    void shouldReturnEmptyColor() {
        assertThat(unitUnderTest.findById(colorFixture.nonExistent.getId())).isEmpty();
    }

    @Test
    void shouldReturnEmptyColorList() {
        assertThat(unitUnderTest.findAllAssociatedWith(userFixture.nonExistent.getId())).isEmpty();
    }

    @Test
    void shouldReturn3ColorsForJdoeUser() {
        List<Color> colorsForJdoeUser = unitUnderTest.findAllAssociatedWith(userFixture.jdoe.getId());
        assertThat(colorsForJdoeUser).isEqualTo(List.of(colorFixture.sulyom, colorFixture.havasiGyopar));
    }
}