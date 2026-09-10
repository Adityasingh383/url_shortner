package com.example.urlshortener.exception;

public class CustomCodeAlreadyExistsException extends RuntimeException {

    public CustomCodeAlreadyExistsException(String message) {
        super(message);
    }
}
