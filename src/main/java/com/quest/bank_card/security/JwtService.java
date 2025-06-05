package com.quest.bank_card.security;

import com.quest.bank_card.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    @Value("${jwt.expiration}")
    private String expTime;
    @Value("${jwt.issuer}")
    private String issuer;

    public String generateJwtToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(Long.parseLong(expTime));

        List<String> authorities = List.of(user.getRole());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(String.valueOf(user.getId()))
                .claim("name", user.getName())
                .claim("login", user.getLogin())
                .claim("authorities", authorities)
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
