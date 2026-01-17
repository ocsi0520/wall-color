package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorCreationRequest;
import com.my_wall_color.color_manager.color.domain.ColorField;
import com.my_wall_color.color_manager.color.usecase.ColorService;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.adapter.web.WebSortAndPaginationMapper;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.PageDto;
import com.my_wall_color.color_manager.shared.sorting_and_pagination.domain.SortAndPagination;
import java.net.URI;
import java.security.Principal;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/color")
@RequiredArgsConstructor
public class ColorController {
  private final ColorService colorService;
  private final WebSortAndPaginationMapper<ColorField> sortAndPaginationMapper;
  @Value("${spring.data.web.pageable.max-page-index}")
  private Integer maxPageIndex;

  @GetMapping("/{id}")
  public ResponseEntity<Color> getColorById(@PathVariable int id) {
    return ResponseEntity.of(colorService.findColorById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Color> createColor(@RequestBody ColorCreationRequest creationRequest,
                                           Principal principal) {
    try {
      var newColor = colorService.createColor(creationRequest, principal.getName());
      var uriOfNewColor = URI.create("/api/color/" + newColor.getId());
      return ResponseEntity.created(uriOfNewColor).body(newColor);
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.of(
          ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Color already exists")).build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.of(
              ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()))
          .build();
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteColor(@PathVariable int id) {
    try {
      colorService.deleteColorBy(id);
      return ResponseEntity.ok().build();
    } catch (NoSuchElementException e) {
      return ResponseEntity.of(ProblemDetail.forStatus(HttpStatus.NOT_FOUND)).build();
    }

  }

  @GetMapping
  public ResponseEntity<PageDto<Color>> getAllColors(Pageable pageable) {
    // TODO: this is a separate concern to normalize page requests
    var normalizedPageRequest = PageRequest.of(
        Math.min(pageable.getPageNumber(), maxPageIndex),
        pageable.getPageSize(),
        pageable.getSort()
    );
    SortAndPagination<ColorField> sortAndPaginationInfo =
        sortAndPaginationMapper.map(normalizedPageRequest);
    PageDto<Color> page = colorService.findAll(sortAndPaginationInfo);
    return ResponseEntity.ok(page);
  }
}
