package com.example.parserNews.apiWork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MetaItem(
        String name,
        String value
) {}
