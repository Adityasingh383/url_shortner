package com.example.urlshortener.service;

import com.example.urlshortener.dto.CreateUrlRequest;
import com.example.urlshortener.dto.UpdateUrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.entity.Url;
import com.example.urlshortener.exception.CustomCodeAlreadyExistsException;
import com.example.urlshortener.exception.ShortCodeNotFoundException;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.util.ShortCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    // Constructor injection — Spring passes in the repository bean automatically
    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public UrlResponse createShortUrl(CreateUrlRequest request) {
        String shortCode;

        if (request.getCustomCode() != null && !request.getCustomCode().trim().isEmpty()) {
            shortCode = request.getCustomCode().trim();

            if (urlRepository.existsByShortCode(shortCode)) {
                throw new CustomCodeAlreadyExistsException(
                        "Custom code already exists: " + shortCode);
            }
        } else {
            shortCode = generateUniqueShortCode();
        }

        Url url = new Url(request.getOriginalUrl(), shortCode, request.getExpiryAt());
        Url savedUrl = urlRepository.save(url);

        return mapToResponse(savedUrl);
    }

    // Keeps generating a random code until it finds one that is not already in use
    private String generateUniqueShortCode() {
        String code = ShortCodeGenerator.generateCode();

        while (urlRepository.existsByShortCode(code)) {
            code = ShortCodeGenerator.generateCode();
        }

        return code;
    }

    @Override
    public Url getUrlByShortCode(String shortCode) {
        Optional<Url> urlOptional = urlRepository.findByShortCode(shortCode);

        if (!urlOptional.isPresent()) {
            throw new ShortCodeNotFoundException("Short code not found: " + shortCode);
        }

        return urlOptional.get();
    }

    @Override
    public UrlResponse getUrlDetails(String shortCode) {
        Url url = getUrlByShortCode(shortCode);
        return mapToResponse(url);
    }

    @Override
    public List<UrlResponse> getAllUrls() {
        List<Url> urls = urlRepository.findAll();
        List<UrlResponse> responses = new ArrayList<>();

        for (Url url : urls) {
            responses.add(mapToResponse(url));
        }

        return responses;
    }

    @Override
    public UrlResponse updateUrl(String shortCode, UpdateUrlRequest request) {
        Url url = getUrlByShortCode(shortCode);

        if (request.getOriginalUrl() != null && !request.getOriginalUrl().trim().isEmpty()) {
            url.setOriginalUrl(request.getOriginalUrl());
        }

        if (request.getExpiryAt() != null) {
            url.setExpiryAt(request.getExpiryAt());
        }

        Url updatedUrl = urlRepository.save(url);
        return mapToResponse(updatedUrl);
    }

    @Override
    public void deleteUrl(String shortCode) {
        Url url = getUrlByShortCode(shortCode);
        urlRepository.delete(url);
    }

    @Override
    public void incrementClickCount(Url url) {
        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);
    }

    private UrlResponse mapToResponse(Url url) {
        return new UrlResponse(
                url.getOriginalUrl(),
                url.getShortCode(),
                url.getExpiryAt(),
                url.getClickCount(),
                url.getCreatedAt()
        );
    }
}
