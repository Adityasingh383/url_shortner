package com.example.urlshortener.dto;

import java.time.LocalDateTime;

public class UpdateUrlRequest {

    // Both fields are optional here — only the ones that are sent get updated.
    // shortCode is deliberately not included, since it must never change.
    private String originalUrl;

    private LocalDateTime expiryAt;

    public UpdateUrlRequest() {
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }
}
