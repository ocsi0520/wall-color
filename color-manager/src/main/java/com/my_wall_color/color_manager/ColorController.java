package com.my_wall_color.color_manager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/color")
public class ColorController {

    @GetMapping("/color/{id}")
    private ResponseEntity<String> getColorById(@PathVariable long id) {
        return ResponseEntity.notFound().build();
    }
}
