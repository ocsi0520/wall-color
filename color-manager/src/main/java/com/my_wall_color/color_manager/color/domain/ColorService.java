package com.my_wall_color.color_manager.color.domain;

import com.my_wall_color.color_manager.color.adapter.web.ColorAlreadyAssignedException;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDTO;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
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

    public Color createColor(ColorCreationRequest request, String name) throws NoSuchElementException, DataIntegrityViolationException, IllegalArgumentException {
        Optional<User> foundUser = userRepository.findByUsername(name);
        User creator = foundUser.orElseThrow(() -> new NoSuchElementException("No user is found with username:" + name));
        Color newColor = Color.create(null, request.red(), request.green(), request.blue(), request.name(), creator.getId());
        return colorRepository.save(newColor);
    }

    @Transactional
    public void deleteColorBy(int id) throws NoSuchElementException {
        if (colorRepository.findById(id).isEmpty()) throw new NoSuchElementException();
        colorRepository.removeBy(id);
    }

    // TODO: unit test
    public List<Color> getAllAssignedColorsFor(String username) throws NoSuchElementException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new NoSuchElementException("No user was found with username: " + username);
        return colorRepository.findAllAssociatedWith(user.get().getId());
    }

    // TODO: unit test
    public void assignColorToUser(int colorId, String username) throws ColorAlreadyAssignedException, NoSuchElementException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new NoSuchElementException("No user was found with username: " + username);
        Integer userId = user.get().getId();

        var color = colorRepository.findById(colorId);
        if (color.isEmpty()) throw new NoSuchElementException("No color was found with id: " + colorId);

        var alreadyAssigned = colorRepository.findAllAssociatedWith(userId).stream().anyMatch(foundColor -> foundColor.getId().equals(colorId));
        if (alreadyAssigned) throw new ColorAlreadyAssignedException(colorId, username);

        colorRepository.assignToUser(color.get(), userId);
    }

    // TODO: unit test
    public void removeAssignmentFromUser(int colorId, String username) throws NoSuchElementException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) throw new NoSuchElementException("No user was found with username: " + username);
        Integer userId = user.get().getId();

        var color = colorRepository.findById(colorId);
        if (color.isEmpty()) throw new NoSuchElementException("No color was found with id: " + colorId);

        var isAssigned = colorRepository.findAllAssociatedWith(userId).stream().anyMatch(foundColor -> foundColor.getId().equals(colorId));
        if (!isAssigned) throw new NoSuchElementException("Color was not assigned to user");

        colorRepository.removeAssignment(color.get(), userId);
    }
}
