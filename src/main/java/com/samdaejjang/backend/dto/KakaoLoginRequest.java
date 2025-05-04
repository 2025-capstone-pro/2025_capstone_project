package com.samdaejjang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoLoginRequest {

    @JsonProperty("id")
    private Long kakaoId;

    private String email;

    private String nickname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String accessToken;
}
