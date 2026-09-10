package com.example.urlshortener.controller;

import com.example.urlshortener.entity.Url;
import com.example.urlshortener.exception.UrlExpiredException;
import com.example.urlshortener.service.UrlService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

// Kept separate from UrlController because this endpoint lives at the root
// path ("/{shortCode}"), not under "/api/urls".
@RestController
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    // GET /{shortCode} -> 302 redirect to the original URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        Url url = urlService.getUrlByShortCode(shortCode);

        if (url.getExpiryAt() != null && url.getExpiryAt().isBefore(LocalDateTime.now())) {
            throw new UrlExpiredException("This short URL has expired: " + shortCode);
        }

        urlService.incrementClickCount(url);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, url.getOriginalUrl());
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
