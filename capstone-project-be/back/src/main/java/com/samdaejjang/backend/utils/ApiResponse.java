package com.samdaejjang.backend.utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({ "success", "data" })  // 순서를 명시
public abstract class ApiResponse <T>{

    private boolean success;
    private T data;

    public ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
