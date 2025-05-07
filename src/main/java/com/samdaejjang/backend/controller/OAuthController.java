package com.samdaejjang.backend.controller;

import com.samdaejjang.backend.dto.AuthResponse;
import com.samdaejjang.backend.dto.KakaoLoginRequest;
import com.samdaejjang.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final AuthService kakaoLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody KakaoLoginRequest request) throws IOException, InterruptedException {

        AuthResponse response = kakaoLoginService.loginOrRegister(request);
        return ResponseEntity.ok(response);
    }
}


