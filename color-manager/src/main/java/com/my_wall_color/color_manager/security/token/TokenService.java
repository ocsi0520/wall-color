package com.my_wall_color.color_manager.security.token;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
  private final JwtEncoder encoder;
  private final Clock clock;
  private final Duration maxAge;
  public static String ROLES_CLAIM_NAME = "roles";

  public TokenService(JwtEncoder encoder, Clock clock,
                      @Value("${token.max-age-seconds:3600}") int maxAgeInSeconds) {
    this.encoder = encoder;
    this.clock = clock;
    this.maxAge = Duration.ofSeconds(maxAgeInSeconds);
  }

  public TokenResult generateToken(Authentication authentication) {
    Instant now = Instant.now(clock);
    Instant expiryTime = now.plus(maxAge);
    String roles = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(expiryTime)
        .subject(authentication.getName())
        .claim(ROLES_CLAIM_NAME, roles)
        .build();
    String token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    return new TokenResult(token, maxAge);
  }
}
