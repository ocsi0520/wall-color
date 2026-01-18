package com.my_wall_color.color_manager.color.domain;

import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDto;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// TODO: check whether it's a better option to provide whole User object instead of a plain ID
public interface ColorRepository {
  Optional<Color> findById(Integer id);

  default Color requiredById(Integer id) throws NoSuchElementException {
    return findById(id).orElseThrow(
        () -> new NoSuchElementException("No color was found by id " + id));
  }

  List<Color> findAllAssociatedWith(Integer userId);

  Color save(Color color);

  void assignToUser(Color color, Integer userId);

  PageDto<Color> findAll(SortAndPagination<ColorField> sortAndPagination);

  void removeBy(Integer id);

  void removeAssignment(Color color, Integer userId);
}
