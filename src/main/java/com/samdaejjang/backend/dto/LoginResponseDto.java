package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {

    private String jwt;
    private Long id;
    private String username;
    private String nickname;
}
