package com.my_wall_color.color_manager;

import com.my_wall_color.color_manager.security.LoginRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.my_wall_color.color_manager.security.CookieBearerTokenResolver.TOKEN_COOKIE_NAME;

public interface AuthHeaderRetriever {
    private String retrieveTokenWith(TestRestTemplate restTemplate) {
        LoginRequest loginRequest = new LoginRequest("jdoe", "user1");
        ResponseEntity<String> tokenResponse = restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);

        List<String> cookies = tokenResponse.getHeaders().get(HttpHeaders.SET_COOKIE);

        return cookies.stream()
                .filter(c -> c.startsWith(TOKEN_COOKIE_NAME))
                .map(c -> c.substring(TOKEN_COOKIE_NAME.length() + 1, c.contains(";") ? c.indexOf(';') : c.length()))
                .findFirst()
                .orElse(null);
    }

    default HttpHeaders getAuthIncludedHeadersWith(TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("cookie", TOKEN_COOKIE_NAME + '=' + retrieveTokenWith(restTemplate));
        return headers;
    }
}
