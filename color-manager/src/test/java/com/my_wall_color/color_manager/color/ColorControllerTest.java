package com.my_wall_color.color_manager.color;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my_wall_color.color_manager.TestProfile;
import com.my_wall_color.color_manager.color.jpa.JpaColor;
import com.my_wall_color.test_utils.PostgresContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles(TestProfile.MVC)
// TODO: when test fixture is done, use actual servlet with them instead of @WithMockUser and MVC
class ColorControllerTest extends PostgresContainerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/color/9999")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void shouldReturnColor() throws Exception {
        var response = mockMvc.perform(get("/api/color/1")).andExpect(status().isOk()).andReturn();
        var readColor = new ObjectMapper().readValue(response.getResponse().getContentAsString(), JpaColor.class);
        assertThat(readColor.getId()).isEqualTo(1L);
        assertThat(readColor.getName()).isEqualTo("Sulyom");
        assertThat(readColor.getRecordedBy()).isEqualTo(1);
    }
}