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

    // TODO: alter SecurityFilter to read token from cookie instead of Authorization header
    // then return the expires in the body so the frontend knows when to login again
    @PostMapping("/auth/login")
    public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Authentication auth = manager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        TokenResult tokenResult = tokenService.generateToken(auth);
        response.addCookie(createTokenCookieFrom(tokenResult));

        return tokenResult.token();
    }

    // TODO: set domain, probably from env
    private Cookie createTokenCookieFrom(TokenResult tokenResult) {
        Cookie tokenCookie = new Cookie("token", tokenResult.token());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge((int) tokenResult.maxAge().toSeconds());
        return tokenCookie;
    }
}
