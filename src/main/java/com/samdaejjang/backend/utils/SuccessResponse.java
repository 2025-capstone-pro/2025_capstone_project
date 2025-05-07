package com.samdaejjang.backend.utils;

public class SuccessResponse<T> extends ApiResponse<T> {

    public SuccessResponse(T data) {
        super(true, data);
    }
}
