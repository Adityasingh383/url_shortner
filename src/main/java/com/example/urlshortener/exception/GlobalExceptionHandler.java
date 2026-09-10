package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

// @RestControllerAdvice lets us catch exceptions thrown from any controller
// in one place, instead of writing try-catch blocks everywhere.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShortCodeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShortCodeNotFound(ShortCodeNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomCodeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCustomCodeAlreadyExists(CustomCodeAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<ErrorResponse> handleUrlExpired(UrlExpiredException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.GONE.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.GONE);
    }

    // Triggered automatically when @Valid fails on a @RequestBody DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            messages.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
        }

        String combinedMessage = String.join(", ", messages);
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), combinedMessage);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Fallback for anything we did not explicitly handle above
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
