package com.example.parserNews.parser;

import com.example.parserNews.apiWork.Child;
import com.example.parserNews.apiWork.KpApiClient;
import com.example.parserNews.apiWork.MetaItem;
import com.example.parserNews.apiWork.RootResponse;
import com.example.parserNews.model.Article;
import com.example.parserNews.repo.ArticleRepository;

import java.io.IOException;

public class ParseMethods {
    public static void main(String[] args) throws IOException, InterruptedException {
        ArticleRepository repo = new ArticleRepository("articles.db");

        KpApiClient apiClient = new KpApiClient();
        RootResponse page = apiClient.fetchPage(2025, 10, 1);

        ArticleFactory factory = new ArticleFactory();

        /*for(Child c : r.childs()){
            System.out.println("ID: " + c.id());
            System.out.println("Title: " + c.ru().title());
            System.out.println("Description: " +
                    htmlParser.extractCleanText("https://www.hab.kp.ru/online/news/" +
                            c.id()));
            System.out.println("Published at: " + extractPublishedAt(c));
        }*/

        /*for (Child ch : r.childs()) {
            Article art = factory.fromJson(ch);
            if (art != null) {
                System.out.println("ID = " + art.getGuid());
                System.out.println("Title = " + art.getTitle());
                System.out.println("LEN = " + art.getDescription().length());
                System.out.println("UTC = " + art.getCreatedAtUtc());
                System.out.println("-----");
            }
        }*/

        for(Child c : page.childs()){
            Article article = factory.fromJson(c);

            if(article == null){
                System.out.println("SKIP: No text");
                continue;
            }

            boolean inserted = repo.save(article);
            if(inserted){
                System.out.println("SAVED: " + article.getGuid());
            } else {
                System.out.println("FAILED, dublicate skipped: " + article.getGuid());
            }
        }
    }

    public static String extractPublishedAt(Child child) {
        if (child.meta() == null) return null;

        return child.meta().stream()
                .filter(m -> "published".equals(m.name()))
                .map(MetaItem::value)
                .findFirst().orElse(null);
    }
}
