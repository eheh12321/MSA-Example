package com.example.eureka_gateway.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenizer {

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 토큰 파싱
    public Jws<Claims> getClaims(String jws) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(jws);
    }

    // 토큰 유효성 검증 -> 파싱 중 에러가 터지면 유효하지 않은 토큰이라는 뜻
    public boolean isValidToken(String jws) {
        try {
            getClaims(jws);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
