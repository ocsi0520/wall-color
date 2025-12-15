package com.my_wall_color.color_manager;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DummyController {

    @GetMapping("/asd")
    public String bla() {
        return "asd";
    }
}
