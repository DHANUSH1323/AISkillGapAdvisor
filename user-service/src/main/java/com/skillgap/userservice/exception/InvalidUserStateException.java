package com.skillgap.userservice.exception;

public class InvalidUserStateException extends RuntimeException {

    public InvalidUserStateException(String message) {
        super(message);
    }
}