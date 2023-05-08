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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtValidateFilter implements GlobalFilter {

    private final JwtTokenizer jwtTokenizer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExceptionRequest(request)) { // 예외 요청인 경우 필터를 거치지 않음
            return chain.filter(exchange);
        }
        Optional<String> auth = getHttpHeader(request, "Authorization");
        if (auth.isPresent()) { // Authorization 헤더가 있는 경우
            if (!isValidToken(auth.get())) {
                return onError(exchange, HttpStatus.UNAUTHORIZED, "허가되지 않은 토큰입니다.");
            }
        } else { // Authorization 헤더가 없는 경우
            return onError(exchange, HttpStatus.UNAUTHORIZED, "토큰이 없습니다.");
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus, String errorMsg) {
        log.error("# Error: {}", errorMsg);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isValidToken(String jws) {
        if (!jws.startsWith("Bearer ")) {
            return false;
        }
        return jwtTokenizer.isValidToken(jws.replace("Bearer ", ""));
    }

    private boolean isExceptionRequest(ServerHttpRequest request) {
        Optional<String> referer = getHttpHeader(request, "Referer");
        Optional<String> host = getHttpHeader(request, "Host");
        if (referer.isPresent()) {
            return referer.get().endsWith("/swagger-ui/index.html");
        }
        if (host.isPresent()) {
            String requestPath = request.getURI().toString().replace(String.format("http://%s/", host.get()), "");
            return requestPath.startsWith("auth/");
        }
        return false;
    }

    private Optional<String> getHttpHeader(ServerHttpRequest request, String headerName) {
        List<String> headers = request.getHeaders().get(headerName);
        if (headers == null || headers.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(headers.get(0));
    }
}
