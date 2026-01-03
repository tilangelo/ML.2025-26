package com.example.parserNews.parser;

import com.example.parserNews.apiWork.Child;
import com.example.parserNews.model.Article;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArticleFactory {

    private final ArticleHtmlParser htmlParser = new ArticleHtmlParser();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Article fromJson(Child child) {
        Long id = child.id();
        String title = child.ru().title();
        String publishedRaw = ParseMethods.extractPublishedAt(child);

        LocalDateTime publishedAt = LocalDateTime.parse(publishedRaw, formatter);

        // Формируем URL статьи
        String url = "https://www.hab.kp.ru/online/news/" + id + "/";

        // Парсим HTML
        String cleanText = null;
        try {
            cleanText = htmlParser.extractCleanText(url);
        } catch (Exception e) {
            System.err.println("HTML parsing failed for ID = " + id + " | " + e.getMessage());
        }

        if (cleanText == null) {
            return null; // в задание входит пропуск статей без текста
        }

        return new Article(id, url, title, cleanText, publishedAt);
    }
}
