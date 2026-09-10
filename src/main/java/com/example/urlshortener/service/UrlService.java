package com.example.urlshortener.service;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.UpdateUrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.entity.Url;

import java.util.List;

public interface UrlService {

    UrlResponse createShortUrl(CreateUrlRequest request);

    // Returns the raw entity — used internally by the redirect controller,
    // which needs to update clickCount and check expiryAt.
    Url getUrlByShortCode(String shortCode);

    UrlResponse getUrlDetails(String shortCode);

    List<UrlResponse> getAllUrls();

    UrlResponse updateUrl(String shortCode, UpdateUrlRequest request);

    void deleteUrl(String shortCode);

    void incrementClickCount(Url url);
}
