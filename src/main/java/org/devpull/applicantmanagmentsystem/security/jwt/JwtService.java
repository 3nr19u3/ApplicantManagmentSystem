package org.devpull.applicantmanagmentsystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtService {
    private final Key key;
    private final String issuer;

    public JwtService(String secret, String issuer) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token);
    }

    public String mintToken(String email, long ttlMinutes) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .subject(email) // sub = email
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlMinutes * 60)))
                .signWith(key)
                .compact();
    }
}
