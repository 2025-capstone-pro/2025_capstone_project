package com.samdaejjang.backend.dto;

import lombok.Data;

@Data
public class RoutineResponseDto {
    private String answer; // LLM이 생성한 텍스트
}
