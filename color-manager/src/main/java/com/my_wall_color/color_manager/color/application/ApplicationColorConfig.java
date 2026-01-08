package com.my_wall_color.color_manager.color.application;

import com.my_wall_color.color_manager.color.domain.ColorService;
import com.my_wall_color.color_manager.color.domain.ColorRepository;
import com.my_wall_color.color_manager.user.domain.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationColorConfig {
    @Bean
    ColorService colorService(ColorRepository cr, UserRepository ur) {
        return new ColorService(cr, ur);
    }
}
