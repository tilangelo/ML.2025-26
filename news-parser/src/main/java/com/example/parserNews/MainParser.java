package com.example.parserNews;

import com.example.parserNews.apiWork.KpApiClient;
import com.example.parserNews.parser.ArticleFactory;
import com.example.parserNews.parser.MassCollector;
import com.example.parserNews.repo.ArticleRepository;

public class MainParser {
    public static void main(String[] args) {
        KpApiClient client = new KpApiClient();
        ArticleFactory factory = new ArticleFactory();
        ArticleRepository repository =
                new ArticleRepository("articles.db");

        MassCollector collector =
                new MassCollector(client, factory, repository);

        collector.collect(2025, 2025);
    }
}
