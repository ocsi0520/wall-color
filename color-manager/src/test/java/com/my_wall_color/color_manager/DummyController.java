package com.my_wall_color.color_manager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DummyController {

    @GetMapping("/hello-world/{whatever}")
    public String index(@PathVariable(value = "whatever", required = false) String whatever) {
        return "<h1>Hello</h1><h2>world!</h2>";
    }

    @GetMapping("/asd")
    public String bla() {
        return "asd";
    }
}
