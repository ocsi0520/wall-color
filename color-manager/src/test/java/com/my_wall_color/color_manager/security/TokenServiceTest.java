package com.my_wall_color.color_manager.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Test
    void shouldReturnTokenAndExpiryDate() {
        // given
        Clock clock = Mockito.mock(Clock.class);
        when(clock.instant()).thenReturn(Instant.parse("2015-10-21T08:28:17Z"));
        JwtEncoder encoder = Mockito.mock(JwtEncoder.class);
        Jwt encodingResult = Mockito.mock(Jwt.class);
        int maxAgeInSeconds = 1800;

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        when(encoder.encode(any())).thenReturn(encodingResult);
        when(encodingResult.getTokenValue()).thenReturn("fake.jwt.token");

        var unitUnderTest = new TokenService(encoder, clock, maxAgeInSeconds);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "jdoe",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );

        // when
        TokenResult result = unitUnderTest.generateToken(auth);

        // then
        verify(encoder).encode(captor.capture());
        var passedClaims = captor.getValue().getClaims();

        assertThat(result.token()).isEqualTo("fake.jwt.token");
        assertThat(result.maxAge().toSeconds()).isEqualTo(maxAgeInSeconds);

        assertThat(passedClaims.getClaimAsString("iss")).isEqualTo("self");
        assertThat(passedClaims.getSubject()).isEqualTo("jdoe");
        assertThat(passedClaims.getClaim("scope").toString()).isEqualTo("ROLE_USER ROLE_ADMIN");
    }
}