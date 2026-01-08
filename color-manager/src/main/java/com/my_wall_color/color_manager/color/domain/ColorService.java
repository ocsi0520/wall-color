package com.my_wall_color.color_manager.color.domain;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
    private final UserRepository userRepository;

    public Optional<Color> findColorById(int colorId) {
        return colorRepository.findById(colorId);
    }

    public PageDTO<Color> findAll(SortAndPagination<ColorField> sortAndPaginationInfo) {
        return colorRepository.findAll(sortAndPaginationInfo);
    }

    public Color createColor(ColorCreationRequest request, String name) throws NoSuchElementException {
        Optional<User> foundUser = userRepository.findByUsername(name);
        User creator = foundUser.orElseThrow(() -> new NoSuchElementException("No user is found with username:" + name));
        Color newColor = Color.create(null, request.red(), request.green(), request.blue(), request.name(), creator.getId());
        return colorRepository.save(newColor);
    }
}
