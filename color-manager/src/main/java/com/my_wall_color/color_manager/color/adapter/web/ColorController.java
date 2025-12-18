package com.my_wall_color.color_manager.color.adapter.web;

import com.my_wall_color.color_manager.color.domain.Color;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/color")
@AllArgsConstructor
public class ColorController {
    private ColorRepository colorRepository;

    @GetMapping("/{id}")
    private ResponseEntity<Color> getColorById(@PathVariable int id) {
        return ResponseEntity.of(colorRepository.findById(id));
    }
}
