package com.example.parserNews.repo;

import com.example.parserNews.model.Article;

import java.sql.*;

public class ArticleRepository {
    private final String url;

    public ArticleRepository(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
        init();
    }

    private void init() {
        String sql = """
            CREATE TABLE IF NOT EXISTS articles (
                guid            INTEGER PRIMARY KEY,
                url             TEXT NOT NULL,
                title           TEXT NOT NULL,
                description     TEXT NOT NULL,
                published_at    TEXT NOT NULL,
                comments_count  INTEGER,
                rating          INTEGER,
                created_at_utc  TEXT NOT NULL
            );
            """;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to init SQLite", e);
        }
    }

    public boolean save(Article article) {
        String sql = """
            INSERT INTO articles(
                guid,
                url,
                title,
                description,
                published_at,
                comments_count,
                rating,
                created_at_utc
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, article.getGuid());
            ps.setString(2, article.getUrl());
            ps.setString(3, article.getTitle());
            ps.setString(4, article.getDescription());
            ps.setString(5, article.getPublishedAt().toString());
            ps.setObject(6, article.getCommentsCount());
            ps.setObject(7, article.getRating());
            ps.setString(8, article.getCreatedAtUtc().toString());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {

            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("Duplicate article: " + article.getGuid());
                return false;
            }
            
            System.err.println("SQLite error for article " + article.getGuid());
            e.printStackTrace();

            return false;
        }
    }
}
