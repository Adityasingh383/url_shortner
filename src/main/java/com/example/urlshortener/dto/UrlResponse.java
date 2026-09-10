package com.example.urlshortener.dto;

import java.time.LocalDateTime;

// We never return the Url entity directly from the controller.
// This DTO controls exactly what shape of JSON goes back to the client.
public class UrlResponse {

    private String originalUrl;
    private String shortCode;
    private LocalDateTime expiryAt;
    private Integer clickCount;
    private LocalDateTime createdAt;

    public UrlResponse() {
    }

    public UrlResponse(String originalUrl, String shortCode, LocalDateTime expiryAt,
                        Integer clickCount, LocalDateTime createdAt) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.expiryAt = expiryAt;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
