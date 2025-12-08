package com.my_wall_color.color_manager.security;
import java.time.Duration;

public record TokenResult(String token, Duration maxAge) {}
