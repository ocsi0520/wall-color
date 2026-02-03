package com.my_wall_color.color_manager.security.presentation;

import com.my_wall_color.color_manager.security.token.CookieBearerTokenResolver;
import com.my_wall_color.color_manager.security.token.TokenResult;
import com.my_wall_color.color_manager.security.token.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.Instant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
  private final AuthenticationManager manager;
  private final TokenService tokenService;
  private final Clock clock;

  public AuthenticationController(
      AuthenticationManager manager,
      TokenService tokenService,
      Clock clock
  ) {
    this.manager = manager;
    this.tokenService = tokenService;
    this.clock = clock;
  }

  @PostMapping("/auth/login")
  public String login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    Authentication auth = manager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
    );
    TokenResult tokenResult = tokenService.generateToken(auth);
    response.addCookie(createTokenCookieFrom(tokenResult));

    return Instant.now(clock).plus(tokenResult.maxAge()).toString();
  }

  private Cookie createTokenCookieFrom(TokenResult tokenResult) {
    Cookie tokenCookie =
        new Cookie(CookieBearerTokenResolver.TOKEN_COOKIE_NAME, tokenResult.token());
    tokenCookie.setSecure(true);
    tokenCookie.setHttpOnly(true);
    tokenCookie.setMaxAge((int) tokenResult.maxAge().toSeconds());
    return tokenCookie;
  }
}
