package com.example.parserNews.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleHtmlParser {

    public String extractCleanText(String url) throws IOException {

        String[] blocked = {"- ВКонтакте;", "- Одноклассники;",
                "- Telegram;", "Почта: red.habkp@phkp.ru",
                "Стали свидетелем интересного события? Сообщите нам об этом:", "- Дзен.",
                "WhatsApp: +7 962-223-38-83"};

        Connection.Response response = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121 Safari/537.36")
                .timeout(20000)
                .ignoreHttpErrors(true)
                .followRedirects(true)
                .ignoreContentType(true)
                .execute();

        System.out.println("STATUS CODE" + response.statusCode());
        //System.out.println("BODY" + response.body());

        String contentType = response.contentType();

        /*if (contentType == null || !contentType.contains("text/html")) {
            // статья мультимедийная / редирект / битая – пропускаем
            return null;
        }*/

        Document doc = response.parse();
        Elements paragraphs = doc.select("p[class^=sc-1wayp1z-]");

        System.out.println("paragraphs count: " + paragraphs.size());

        //Если параграфы не найдены, поиск по дивам
        if (paragraphs.isEmpty()) {
            // fallback #1
            paragraphs = doc.select("div[data-qa=article-text] p");
        }

        // fallback #2 — иногда текст лежит прямо в article-body
        if (paragraphs.isEmpty()) {
            paragraphs = doc.select("article p");
        }

        if (paragraphs.isEmpty()) {
            return null; // текстовая часть не найдена
        }

        StringBuilder sb = new StringBuilder();

        for(Element paragraph : paragraphs) {
            String text = paragraph.text().trim();
            boolean flag = false;

            for(String word: blocked){
                if(text.equals(word)){
                    flag = true;
                    break;
                }
            }

            if(!text.isEmpty() && !flag) {
                sb.append(text).append("\n\n");
            }
        }

        String cleanText = sb.toString().trim();

        if(cleanText.length() < 50){
            return null;
        }

        return cleanText;
    }
}
