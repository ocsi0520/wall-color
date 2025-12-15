package com.my_wall_color.color_manager.security.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Arrays;

public class CookieBearerTokenResolver implements BearerTokenResolver {
    public static final String TOKEN_COOKIE_NAME = "token";

    @Override
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
