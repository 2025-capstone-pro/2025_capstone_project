package com.samdaejjang.backend.service;

import com.samdaejjang.backend.config.JwtTokenProvider;
import com.samdaejjang.backend.dto.AuthResponse;
import com.samdaejjang.backend.dto.KakaoLoginRequest;
import com.samdaejjang.backend.entity.Users;
import com.samdaejjang.backend.repository.UsersRepository;
import com.samdaejjang.backend.utils.AuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse loginOrRegister(KakaoLoginRequest request) {
        
        // 사용자 정보로 회원가입 OR 로그인 처리
        Users user = registerOrFindUser(request);

        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(user.getUsername());

        return new AuthResponse(true, new AuthResponse.DataPayload(token, user.getUserId()));
    }

    private Users registerOrFindUser(KakaoLoginRequest request) {
        return usersRepository.findByUsername(request.getEmail())

                // 기존에 있는 등록된 사용자이면
                .map(existingUser -> {
                    if (!AuthProvider.KAKAO.equals(existingUser.getProvider())) {
                        throw new IllegalStateException("다른 로그인 방식으로 이미 가입된 이메일입니다.");
                    }

                    return existingUser;
                })

                // 기존에 등록된 사용자가 아닌 새로운 사용자라면
                .orElseGet(() -> {
                    Users newUser = new Users();
                    newUser.setUsername(request.getEmail());
                    newUser.setNickname(request.getNickname());
                    newUser.setProvider(AuthProvider.KAKAO);
                    newUser.setPhoneNumber(request.getPhoneNumber());
                    return usersRepository.save(newUser);
                });
    }

    /*private KakaoTokenResponse getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code); // 인가코드

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        return restTemplate.postForObject("https://kauth.kakao.com/oauth/token", request, KakaoTokenResponse.class
        );
    }*/

    /*private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        return restTemplate.exchange("https://kapi.kakao.com/v2/user/me" ,HttpMethod.GET, request, KakaoUserInfo.class).getBody();
    }*/


}
