package com.example.parserNews.apiWork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Child(
        @JsonProperty("@id") long id,
        RuBlock ru,
        List<MetaItem> meta
) { }
