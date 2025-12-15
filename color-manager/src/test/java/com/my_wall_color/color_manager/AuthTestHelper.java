package com.my_wall_color.color_manager;

import com.my_wall_color.color_manager.security.presentation.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.my_wall_color.color_manager.security.token.CookieBearerTokenResolver.TOKEN_COOKIE_NAME;

@Component
@Profile(TestProfile.NON_MVC)
public class AuthTestHelper {
    private static final String JWT_FROM_JWT_IO = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

    @Autowired
    TestRestTemplate restTemplate;

    public ResponseEntity<String> getSuccessAuthResponse() {
        LoginRequest loginRequest = new LoginRequest("jdoe", "user1");
        return restTemplate.postForEntity("/api/auth/login", loginRequest, String.class);
    }

    public ResponseEntity<Void> getWrongCredentialsAuthResponse() {
        LoginRequest loginRequest = new LoginRequest("non-existent-user", "not-valid-password");
        return restTemplate.postForEntity("/api/auth/login", loginRequest, Void.class);
    }

    public HttpHeaders getInvalidAuthIncludedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("cookie", TOKEN_COOKIE_NAME + '=' + JWT_FROM_JWT_IO);
        return headers;
    }

    public HttpHeaders getValidAuthIncludedHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("cookie", TOKEN_COOKIE_NAME + '=' + retrieveToken());
        return headers;
    }

    private String retrieveToken() {
        List<String> cookies = getSuccessAuthResponse().getHeaders().get(HttpHeaders.SET_COOKIE);

        return cookies.stream()
                .filter(c -> c.startsWith(TOKEN_COOKIE_NAME))
                .map(c -> c.substring(TOKEN_COOKIE_NAME.length() + 1, c.contains(";") ? c.indexOf(';') : c.length()))
                .findFirst()
                .orElse(null);
    }
}
