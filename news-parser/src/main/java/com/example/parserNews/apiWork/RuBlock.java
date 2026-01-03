package com.example.parserNews.apiWork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuBlock(
        String title,
        String description
) {}
