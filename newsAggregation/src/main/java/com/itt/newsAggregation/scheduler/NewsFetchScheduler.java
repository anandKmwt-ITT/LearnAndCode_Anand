package com.itt.newsAggregation.scheduler;

import com.itt.newsAggregation.dto.ArticleDto;
import com.itt.newsAggregation.dto.NotificationPreferenceDto;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.NewsFetcher;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class NewsFetchScheduler {

    @Autowired
    private List<NewsFetcher> newsFetchers;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private NotificationPreferenceService notificationPreferenceService;

    @PostConstruct
    public void fetchOnStartup() {
        fetchNewsArticles();
    }

    @Scheduled(cron = "0 0 */4 * * *")
    public void fetchNewsArticles() {
        List<ArticleDto> articles = fetchFromAvailableSources();

        if (!articles.isEmpty()) {
            articleService.saveArticles(articles);
            log.info("Fetched and saved {} articles at {}", articles.size(), LocalDateTime.now());
        } else {
            log.warn("No articles fetched from any configured source.");
        }
    }

    private List<ArticleDto> fetchFromAvailableSources() {
        for (NewsFetcher fetcher : newsFetchers) {
            try {
                List<ArticleDto> articles = fetcher.fetchArticles();
                if (articles != null && !articles.isEmpty()) {
                    log.info("Fetched articles using {}", fetcher.getClass().getSimpleName());
                    return articles;
                }
            } catch (Exception e) {
                log.error("Error using fetcher {}: {}", fetcher.getClass().getSimpleName(), e.getMessage());
            }
        }
        return Collections.emptyList();
    }
}