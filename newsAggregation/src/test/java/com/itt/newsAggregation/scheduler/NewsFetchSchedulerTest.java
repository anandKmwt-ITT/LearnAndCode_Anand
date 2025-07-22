package com.itt.newsAggregation.scheduler;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.external.NewsFetcher;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class NewsFetchSchedulerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private NotificationPreferenceService notificationPreferenceService;

    @Mock
    private NewsFetcher fetcher1;

    @Mock
    private NewsFetcher fetcher2;

    private NewsFetchScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        scheduler = new NewsFetchScheduler(
                List.of(fetcher1, fetcher2),
                articleService,
                notificationPreferenceService
        );
    }

    @Test
    void testFetchNewsArticles_successfullyFetched() {
        ArticleDto article = ArticleDto.builder()
                .title("Test Article")
                .content("Some content")
                .url("http://example.com")
                .publishedAt(LocalDateTime.now())
                .build();
        when(fetcher1.fetchArticles()).thenReturn(List.of(article));

        scheduler.fetchNewsArticles();

        verify(fetcher1, times(1)).fetchArticles();
        verify(fetcher2, never()).fetchArticles();
        verify(articleService).saveArticles(List.of(article));
    }

    @Test
    void testFetchNewsArticles_noneFetched() {
        when(fetcher1.fetchArticles()).thenReturn(Collections.emptyList());
        when(fetcher2.fetchArticles()).thenReturn(Collections.emptyList());

        scheduler.fetchNewsArticles();

        verify(articleService, never()).saveArticles(any());
    }

    @Test
    void testFetchNewsArticles_fetcherThrowsException() {
        when(fetcher1.fetchArticles()).thenThrow(new RuntimeException("API down"));
        when(fetcher2.fetchArticles()).thenReturn(Collections.emptyList());

        scheduler.fetchNewsArticles();

        verify(fetcher1).fetchArticles();
        verify(fetcher2).fetchArticles();
        verify(articleService, never()).saveArticles(any());
    }
}
