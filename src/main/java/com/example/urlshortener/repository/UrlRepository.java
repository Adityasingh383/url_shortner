package com.example.urlshortener.repository;

import com.example.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository already gives us save(), findAll(), findById(), delete(), etc.
// We only need to declare the extra lookup methods that are specific to this project.
public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);
}
