package com.my_wall_color.color_manager.color.usecase;

import com.my_wall_color.color_manager.color.adapter.web.ColorAlreadyAssignedException;
import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorCreationRequest;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDto;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import com.my_wall_color.color_manager.user.domain.User;
import com.my_wall_color.color_manager.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ColorService {
  private final ColorRepository colorRepository;
  private final UserRepository userRepository;

  public Optional<Color> findColorById(int colorId) {
    return colorRepository.findById(colorId);
  }

  public PageDto<Color> findAll(SortAndPagination<ColorField> sortAndPaginationInfo) {
    return colorRepository.findAll(sortAndPaginationInfo);
  }

  public Color createColor(ColorCreationRequest request, String name)
      throws NoSuchElementException, DataIntegrityViolationException, IllegalArgumentException {
    User creator = userRepository.requiredByUsername(name);
    Color newColor =
        Color.create(null, request.red(), request.green(), request.blue(), request.name(),
            creator.getId());
    return colorRepository.save(newColor);
  }

  public void deleteColorBy(int id) throws NoSuchElementException {
    var color = colorRepository.requiredById(id);
    colorRepository.removeBy(color.getId());
  }

  // TODO: unit test
  public List<Color> getAllAssignedColorsFor(String username) throws NoSuchElementException {
    var user = userRepository.requiredByUsername(username);
    return colorRepository.findAllAssociatedWith(user.getId());
  }

  // TODO: unit test
  public void assignColorToUser(int colorId, String username)
      throws ColorAlreadyAssignedException, NoSuchElementException,
      DataIntegrityViolationException {
    var user = userRepository.requiredByUsername(username);
    Integer userId = user.getId();

    var color = colorRepository.requiredById(colorId);

    List<Color> allAssociatedColor = colorRepository.findAllAssociatedWith(userId);
    var alreadyAssigned =
        allAssociatedColor.stream().anyMatch(foundColor -> foundColor.getId().equals(colorId));
    if (alreadyAssigned) {
      throw new ColorAlreadyAssignedException(colorId, username);
    }

    if (allAssociatedColor.size() >= 7) {
      throw new DataIntegrityViolationException("Maximum 7 colors can be assigned to a user");
    }

    colorRepository.assignToUser(color, userId);
  }

  // TODO: unit test
  public void removeAssignmentFromUser(int colorId, String username) throws NoSuchElementException {
    var user = userRepository.requiredByUsername(username);
    Integer userId = user.getId();

    var color = colorRepository.requiredById(colorId);

    var isAssigned = colorRepository.findAllAssociatedWith(userId).stream()
        .anyMatch(foundColor -> foundColor.getId().equals(colorId));
    if (!isAssigned) {
      throw new NoSuchElementException("Color was not assigned to user");
    }

    colorRepository.removeAssignment(color, userId);
  }
}
