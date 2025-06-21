package com.example.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // JWT 검증을 하지 않아도 되는 경로들
    public static final List<String> openApiEndpoints = List.of(
            "/api/oauth/",
            "/api/auth/"
    );

    /**
     * 요청의 URI 가 openApiEndpoints 중 하나와 일치하는지 확인
     * true 리턴 -> 필터를 거치게 됨
     * false 리턴 -> 필터 안거침
     */
    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        for (String uri : openApiEndpoints) {
            if (path.contains(uri)) {
                return false; // 오픈 API 이므로 인증 필요 없음
            }
        }
        return true; // 보호된 API 이므로 인증 필요함
    };
}
