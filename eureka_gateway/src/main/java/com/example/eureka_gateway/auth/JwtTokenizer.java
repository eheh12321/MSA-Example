package com.example.eureka_gateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
@Component
public class JwtTokenizer {

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 토큰 파싱
    public Jws<Claims> getClaims(String jws) {
        return Jwts.parserBuilder()
                .setSigningKey(getEncodedSecretKey())
                .build()
                .parseClaimsJws(jws);
    }

    // 토큰 유효성 검증 -> 파싱 중 에러가 터지면 유효하지 않은 토큰이라는 뜻
    public boolean isValidToken(String jws) {
        try {
            getClaims(jws);
        } catch (Exception e) {
            log.error("# 토큰 파싱 실패 - {}", e.getMessage());
            return false;
        }
        return true;
    }

    private Key getEncodedSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
