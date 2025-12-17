package com.my_wall_color.color_manager.color.jpa;


import com.my_wall_color.color_manager.color.Color;
import com.my_wall_color.color_manager.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JpaColorRepositoryAdapterIntegrationTest extends IntegrationTest {
    @Autowired
    private JpaColorRepositoryAdapter unitUnderTest;

    // TODO: fixture
    Color sulyomColor = new Color(1, (short) 136, (short) 147, (short) 152, "Sulyom", 1);

    @Test
    void shouldReturnSulyom() {
        assertThat(unitUnderTest.findById(1)).contains(sulyomColor);
    }

    @Test
    void shouldReturnEmptyColor() {
        assertThat(unitUnderTest.findById(9999)).isEmpty();
    }

    @Test
    void shouldReturnEmptyColorList() {
        assertThat(unitUnderTest.findAllAssociatedWith(9999)).isEmpty();
    }

    @Test
    void shouldReturn3ColorsForJdoeUser() {
        List<Color> colorsForJdoeUser = unitUnderTest.findAllAssociatedWith(1);
        assertThat(colorsForJdoeUser).hasSize(4);
        assertThat(colorsForJdoeUser.get(0)).isEqualTo(sulyomColor);
        assertThat(colorsForJdoeUser.get(1).getName()).isEqualTo("Brazil menta");
        assertThat(colorsForJdoeUser.get(2).getName()).isEqualTo("Havasi eukaliptusz");
        assertThat(colorsForJdoeUser.get(3).getName()).isEqualTo("Szarkaláb");
    }
}