package com.example.eureka_auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenizer {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    public String delegateAccessToken(long memberId, String username) {
        Map<String, Object> claims = generateClaims(memberId, username);
        Date expiration = getTokenExpiration(accessTokenExpirationMinutes);
        String accessToken = generateAccessToken(claims, expiration);
        return String.format("Bearer %s", accessToken);
    }

    private Map<String, Object> generateClaims(long memberId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberId);
        claims.put("username", username);
        return claims;
    }

    private Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    private String generateAccessToken(Map<String, Object> claims, Date expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject("Test-token")
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(getEncodedSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getEncodedSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
