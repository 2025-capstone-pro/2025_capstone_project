package com.example.gateway.filter;


import com.example.gateway.util.JwtUtil;
import com.example.gateway.validator.RouteValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 전역 필터 - jwt 토큰 검증
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {


    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final JwtUtil jwtUtil;
    private final RouteValidator routeValidator;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("[AuthenticationFilter] 요청 경로: {}", path);

        // 요청이 openApiEndpoints 일 경우 이 필터는 스킵
        if (!routeValidator.isSecured.test(exchange.getRequest())) {
            log.info("[AuthenticationFilter] '{}' 는 인증이 필요 없는 경로입니다. 필터 통과.", path);
            return chain.filter(exchange);
        }

        // 요청에서 Authorization 헤더 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
        log.debug("[AuthenticationFilter] Authorization 헤더: {}", authHeader);


        // 토큰 존재 및 형식 확인
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            log.warn("[AuthenticationFilter] 유효하지 않은 Authorization 헤더. 접근 거부.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // JWT 토큰만 추출
        String token = authHeader.substring(TOKEN_PREFIX.length());
        log.debug("[AuthenticationFilter] JWT 토큰 추출: {}", token);

        // 유효성 검증
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("[AuthenticationFilter] 유효하지 않은 JWT 토큰. 접근 거부.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 클레임에서 userId 추출
        Claims claims = jwtUtil.extractClaims(token);
        Long userId = claims.get("userId", Long.class);

        log.info("[AuthenticationFilter] 토큰 인증 성공. userId: {}", userId);

        // 요청 헤더에 userId 전달
        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", String.valueOf(userId))
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        // 필터 체인 계속 진행
        return chain.filter(mutatedExchange);
    }
}

