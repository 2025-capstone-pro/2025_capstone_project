package com.samdaejjang.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private boolean success;
    private DataPayload data;

    @Data
    @AllArgsConstructor
    public static class DataPayload {
        private String jwt;
        private Long user_id;
    }
}
