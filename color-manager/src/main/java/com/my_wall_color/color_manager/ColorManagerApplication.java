package com.my_wall_color.color_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@SpringBootApplication
public class ColorManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColorManagerApplication.class, args);
	}

    // TODO: log

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
