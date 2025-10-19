package com.my_wall_color.color_manager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("/")
    public String index() {
        return "<h1>Hello</h1><h2>world!</h2>";
    }
}
