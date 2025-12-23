package com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.user.UserFixture;
import com.my_wall_color.color_manager.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageMapperTest {
    @Test
    void shouldHandleEmptyPage() {
        List<User> listOfUsers = List.of();
        var input = new PageImpl(listOfUsers, PageRequest.of(4, 3), 7);
        var unitUnderTest = new PageMapper();
        var actual = unitUnderTest.toDomain(input);
        var expected = new PageDTO<User>(listOfUsers, 4, 3, 7, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldHandleSimplePage() {
        var userFixture = new UserFixture();
        List<User> listOfUsers = List.of(userFixture.donna, userFixture.jdoe, userFixture.alex);
        var input = new PageImpl(listOfUsers, PageRequest.of(0, 3), 5);
        var unitUnderTest = new PageMapper();
        var actual = unitUnderTest.toDomain(input);
        var expected = new PageDTO<User>(listOfUsers, 0, 3, 5, 2);
        assertThat(actual).isEqualTo(expected);
    }
}