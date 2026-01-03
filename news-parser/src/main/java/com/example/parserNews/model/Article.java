package com.example.parserNews.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Article {
    private final long guid;
    private final String url;
    private final String title;
    private final String description;   // очищенный текст
    private final LocalDateTime publishedAt;
    private final Integer commentsCount; // по заданию null
    private final Integer rating;        // по заданию null
    private final LocalDateTime createdAtUtc;

    public Article(long guid,
                   String url,
                   String title,
                   String description,
                   LocalDateTime publishedAt) {

        this.guid = guid;
        this.url = url;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;

        this.commentsCount = null;
        this.rating = null;
        this.createdAtUtc = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
    }

    public long getGuid() {
        return guid;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public Integer getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAtUtc() {
        return createdAtUtc;
    }
}
