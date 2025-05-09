package com.example.gateway.filter;


import com.example.gateway.util.JwtUtil;
import com.example.gateway.validator.RouteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 전역 필터 - jwt 토큰 검증
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {


    private final JwtUtil jwtUtil;
    private final RouteValidator routeValidator;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 요청이 openApiEndpoints 일 경우 이 필터는 스킵
        if (!routeValidator.isSecured.test(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        // 요청에서 Authorization 헤더 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);

        // 토큰 존재 및 형식 확인
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // JWT 토큰만 추출
        String token = authHeader.substring(TOKEN_PREFIX.length());

        // 유효성 검증
        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 필터 체인 계속 진행
        return chain.filter(exchange);
    }
}

