package com.skillgap.userservice.exception;

import com.skillgap.userservice.model.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("USER_NOT_FOUND", ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserConflictException.class)
    public ResponseEntity<ErrorResponse> handleUserConflict(UserConflictException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("USER_CONFLICT", ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(InvalidUserStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidUserStateException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("INVALID_USER_STATE", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse("INTERNAL_ERROR", "Unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}