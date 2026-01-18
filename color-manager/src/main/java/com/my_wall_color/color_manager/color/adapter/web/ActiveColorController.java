package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.usecase.ColorService;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me/active-color")
@RequiredArgsConstructor
public class ActiveColorController {
  private final ColorService colorService;

  @GetMapping
  public List<Color> getAllAssignedColors(Principal principal) {
    return colorService.getAllAssignedColorsFor(principal.getName());
  }

  @PostMapping("/{id}")
  public ResponseEntity<Void> assignColorToUser(@PathVariable("id") int colorId,
                                                Principal principal) {
    try {
      colorService.assignColorToUser(colorId, principal.getName());
      return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) {
      return ResponseEntity.of(
          ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage())).build();
    } catch (ColorAlreadyAssignedException e) {
      return ResponseEntity.of(
          ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage())).build();
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.of(
              ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()))
          .build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> revokeColorFromUser(@PathVariable("id") int colorId,
                                                  Principal principal) {
    try {
      colorService.removeAssignmentFromUser(colorId, principal.getName());
      return ResponseEntity.noContent().build();
    } catch (NoSuchElementException e) {
      return ResponseEntity.of(
          ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage())).build();
    }
  }
}
