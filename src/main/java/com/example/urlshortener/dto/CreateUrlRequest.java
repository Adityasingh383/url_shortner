package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class CreateUrlRequest {

    @NotBlank(message = "originalUrl must not be empty")
    private String originalUrl;

    // Optional: if not provided, the service generates a random short code
    private String customCode;

    // Optional: if not provided, the link never expires
    private LocalDateTime expiryAt;

    public CreateUrlRequest() {
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }
}
