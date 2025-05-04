package com.samdaejjang.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SignupResponseDto {

    @JsonProperty("user_id")
    private Long id;

    private String username;

    private String nickname;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
