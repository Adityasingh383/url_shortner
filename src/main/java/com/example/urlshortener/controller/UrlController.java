package com.example.urlshortener.controller;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.UpdateUrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    // POST /api/urls -> create a new short URL
    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody CreateUrlRequest request) {
        UrlResponse response = urlService.createShortUrl(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET /api/urls/{shortCode} -> fetch details of one short URL
    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> getUrlDetails(@PathVariable String shortCode) {
        UrlResponse response = urlService.getUrlDetails(shortCode);
        return ResponseEntity.ok(response);
    }

    // GET /api/urls -> list every short URL
    @GetMapping
    public ResponseEntity<List<UrlResponse>> getAllUrls() {
        List<UrlResponse> responses = urlService.getAllUrls();
        return ResponseEntity.ok(responses);
    }

    // PUT /api/urls/{shortCode} -> update originalUrl and/or expiryAt only
    @PutMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> updateUrl(@PathVariable String shortCode,
                                                  @RequestBody UpdateUrlRequest request) {
        UrlResponse response = urlService.updateUrl(shortCode, request);
        return ResponseEntity.ok(response);
    }

    // DELETE /api/urls/{shortCode} -> remove the mapping entirely
    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}
