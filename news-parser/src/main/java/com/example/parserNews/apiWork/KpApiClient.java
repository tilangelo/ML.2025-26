package com.example.parserNews.apiWork;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class KpApiClient {
    private static final String BASE_URL =
            "https://s02.api.yc.kpcdn.net/content/api/1/pages/get.json";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public KpApiClient() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        this.mapper = new ObjectMapper();
    }

    public RootResponse fetchPage(int year, int month, int page) throws IOException,
            InterruptedException {

        String url = BASE_URL +
                "?pages.age.month=" + month +
                "&pages.age.year=" + year +
                "&pages.direction=page" +
                "&pages.number=" + page +
                "&pages.target.class=100" +
                "&pages.target.id=43"; // регион Хабаровск

        System.out.println("URL: " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", randomUa())
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Bad status code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), RootResponse.class);
    }

    private String randomUa(){
        List<String> userAgentList = List.of(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 18_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.1 Mobile/15E148 Safari/604.1",
                "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.6778.39 Mobile Safari/537.36"
        );

        Random random = new Random();

        return userAgentList.get(random.nextInt(userAgentList.size()));
    }

}
