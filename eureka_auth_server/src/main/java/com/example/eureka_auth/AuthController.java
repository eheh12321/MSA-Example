package com.example.eureka_auth;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/login")
    public String login(String username, HttpServletResponse response) {
        String accessToken = jwtTokenizer.delegateAccessToken(1L, username);
        response.setHeader("Authorization", accessToken);
        return "OK";
    }
}
