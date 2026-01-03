package com.example.parserNews.parser;

import com.example.parserNews.apiWork.Child;
import com.example.parserNews.apiWork.KpApiClient;
import com.example.parserNews.apiWork.RootResponse;
import com.example.parserNews.model.Article;
import com.example.parserNews.repo.ArticleRepository;
import com.google.common.util.concurrent.RateLimiter;

public class MassCollector {
    private final KpApiClient apiClient;
    private final ArticleFactory factory;
    private final ArticleRepository repository;

    private final RateLimiter apiLimiter = RateLimiter.create(0.5);   // 1 req / 2 sec
    private final RateLimiter htmlLimiter = RateLimiter.create(0.4);  // 1 req / 2.5 sec

    public MassCollector(KpApiClient apiClient,
                         ArticleFactory factory,
                         ArticleRepository repository) {

        this.apiClient = apiClient;
        this.factory = factory;
        this.repository = repository;
    }

    public void collect(int startYear, int endYear) {
        for(int year = startYear; year <= endYear; year++) {
            for (int month = 7; month <= 12; month++) {
                int page = 1;
                System.out.printf("📅 %d-%02d%n", year, month);

                int skippedPages = 0;

                while(true){
                    try {
                        apiLimiter.acquire();

                        RootResponse response = apiClient.fetchPage(year, month, page);

                        if(response == null || response.childs()==null ||
                        response.childs().isEmpty()) {
                            System.out.println("No more pages at: " + year + "-" + month);
                            break;
                        }

                        int failerCount = 0;

                        for(Child child : response.childs()) {

                            htmlLimiter.acquire();

                            Article article = factory.fromJson(child);

                            if(article==null) {
                                continue;
                            }

                            boolean saved = repository.save(article);
                            if(saved) {
                                System.out.println("Saved!!!!!: " + article.getGuid());
                                failerCount = 0;
                            } else {
                                System.out.println("Failed to save!!!!!, article existed already: "
                                        + article.getGuid());
                                failerCount++;
                                System.out.println(failerCount);
                                if (failerCount == 15) {
                                    skippedPages++;
                                    break;
                                }
                            }
                        }

                        if (skippedPages >= 2) {
                            break;
                        }
                        page++;
                    } catch (Exception e) {
                        System.out.println("error: " + e.getMessage());
                        try{
                            Thread.sleep(5000);
                        }catch (InterruptedException ignored) {}
                    }
                }
            }
        }
    }
}
