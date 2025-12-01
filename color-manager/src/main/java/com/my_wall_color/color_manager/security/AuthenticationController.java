package com.my_wall_color.color_manager.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    private AuthenticationManager manager;

    private TokenService tokenService;

    public AuthenticationController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping("/public")
    // TODO: remove
    public String handlePublic() {
        System.out.println("Hello World");
        return "Hello World";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody Map<String, String> user, HttpServletResponse response) {
//        log.debug("User login: {}", user);
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get("username"), user.get("password")));
        String token = tokenService.generateToken(auth);
//        log.debug("JWT token: {}", token);
        var tokenCookie = new Cookie("token", token);
        tokenCookie.setHttpOnly(true); // TODO: setMaxAge
        response.addCookie(tokenCookie);
        return token;
    }
}
