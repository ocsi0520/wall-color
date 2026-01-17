package com.my_wall_color.color_manager.color.usecase;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorCreationRequest;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDto;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortOrderList;
import com.my_wall_color.color_manager.user.UserFixture;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ColorServiceTest {

    ColorService unitUnderTest;
    ColorRepository colorRepository;
    UserRepository userRepository;
    User jdoe;

    @BeforeEach
    void setup() {
        colorRepository = Mockito.mock(ColorRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        unitUnderTest = new ColorService(colorRepository, userRepository);
        jdoe = new UserFixture().jdoe;
    }

    @Test
    void shouldDelegateFindToRepository() {
        var expectedColor = Color.create(1, 0, 0, 0, "black", 1);
        when(colorRepository.findById(1)).thenReturn(Optional.of(expectedColor));
        var response = unitUnderTest.findColorById(1);
        assertThat(response.isPresent()).isTrue();
        assertThat(response.get()).isEqualTo(expectedColor);
    }

    @Test
    void shouldDelegateFindAllToRepository() {
        var expectedPage = new PageDto<Color>(List.of(), 0, 10, 0, 0);
        SortOrderList<ColorField> colorFieldSortOrderList = SortOrderList.of();
        SortAndPagination<ColorField> sortAndPagination = new SortAndPagination<>(10, 0, colorFieldSortOrderList);
        when(colorRepository.findAll(sortAndPagination)).thenReturn(expectedPage);
        var response = unitUnderTest.findAll(sortAndPagination);
        assertThat(response).isEqualTo(expectedPage);
    }

    @Test
    void shouldThrowAsNoUserWasFound() {
        var request = ColorCreationRequest.of(0, 0, 0, "black");
        when(userRepository.findByUsername(jdoe.getUsername())).thenReturn(Optional.empty());
        when(userRepository.requiredByUsername(jdoe.getUsername())).thenCallRealMethod();
        assertThrows(NoSuchElementException.class, () -> unitUnderTest.createColor(request, jdoe.getUsername()));
    }

    @Test
    void shouldDelegateColorCreationToRepository() {
        var request = ColorCreationRequest.of(0, 0, 0, "black");
        var expectedColor = Color.create(1, 0, 0, 0, "black", jdoe.getId());
        when(userRepository.findByUsername(jdoe.getUsername())).thenReturn(Optional.of(jdoe));
        when(userRepository.requiredByUsername(jdoe.getUsername())).thenCallRealMethod();
        ArgumentCaptor<Color> captor = ArgumentCaptor.forClass(Color.class);

        when(colorRepository.save(captor.capture())).thenReturn(expectedColor);
        var actual = unitUnderTest.createColor(request, jdoe.getUsername());
        assertThat(actual).isEqualTo(expectedColor);

        var passedColor = captor.getValue();
        assertThat(passedColor.getId()).isNull();
        assertThat(passedColor.getRed()).isEqualTo(expectedColor.getRed());
        assertThat(passedColor.getGreen()).isEqualTo(expectedColor.getGreen());
        assertThat(passedColor.getBlue()).isEqualTo(expectedColor.getBlue());
        assertThat(passedColor.getName()).isEqualTo(expectedColor.getName());
        assertThat(passedColor.getRecordedBy()).isEqualTo(expectedColor.getRecordedBy());
    }
}