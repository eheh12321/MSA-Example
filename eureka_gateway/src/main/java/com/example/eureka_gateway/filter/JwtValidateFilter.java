package com.example.eureka_gateway.filter;

import com.example.eureka_gateway.auth.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtValidateFilter implements GlobalFilter {

    private final JwtTokenizer jwtTokenizer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getURI().toString().replace("http://localhost:8000/", "");
            if(requestPath.startsWith("auth/")) { // 인증 서버로 가는 요청에 대해서는 처리 X
                return chain.filter(exchange);
            }
            String jws = request.getHeaders().get("Authorization").get(0);
            if (!isValidToken(jws)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "허가되지 않은 토큰입니다.");
            }
        } catch (NullPointerException e) {
            return onError(exchange, HttpStatus.UNAUTHORIZED, "토큰이 없습니다.");
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMsg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error("# Error: {}", errorMsg);
        return response.setComplete();
    }

    private boolean isValidToken(String jws) {
        if (!jws.startsWith("Bearer ")) {
            return false;
        }
        return jwtTokenizer.isValidToken(jws.replace("Bearer ", ""));
    }
}
