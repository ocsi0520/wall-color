package com.my_wall_color.color_manager;

import com.my_wall_color.color_manager.adapter.JpaColor;
import com.my_wall_color.color_manager.adapter.JpaColorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/color")
public class ColorController {

    private JpaColorRepository colorRepository;

    public ColorController(JpaColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @GetMapping("/{id}")
    private ResponseEntity<JpaColor> getColorById(@PathVariable long id) {
        return ResponseEntity.of(this.colorRepository.findById(id));
    }
}
