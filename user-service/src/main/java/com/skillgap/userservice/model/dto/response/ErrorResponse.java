package com.skillgap.userservice.model.dto.response;

import java.time.Instant;

public class ErrorResponse {

    private String errorCode;
    private String message;
    private Instant timestamp;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}