package com.example.parserNews.apiWork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

//рутовый JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public record RootResponse(List<Child> childs) { }