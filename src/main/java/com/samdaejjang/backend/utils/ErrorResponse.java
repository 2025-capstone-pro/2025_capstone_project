package com.samdaejjang.backend.utils;

public class ErrorResponse extends ApiResponse<ErrorResponse.ErrorBody> {

    public ErrorResponse(String message) {
        super(false, new ErrorBody(message));
    }

    public static class ErrorBody {
        private final String message;

        public ErrorBody(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
