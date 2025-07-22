package com.itt.newsAggregation.external;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.service.ApiClientService;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TheNewsApiFetcherTest {

    @InjectMocks
    private TheNewsApiFetcher theNewsApiFetcher;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private RestTemplate restTemplate;

    private final String dummyUrl = "http://thenewsapi.com/feed?apiKey=test";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchArticles_ReturnsValidArticles() throws JSONException {
        JSONArray articleArray = new JSONArray()
                .put(new JSONObject()
                        .put("title", "AI Takes Over")
                        .put("description", "AI is now writing code.")
                        .put("url", "http://example.com/ai")
                        .put("source", "TheNewsAPI")
                        .put("published_at", "2024-06-28T10:15:30Z"));

        JSONObject mockResponseJson = new JSONObject()
                .put("data", articleArray);

        when(apiClientService.getApiClientByName("TheNewsAPI")).thenReturn(dummyUrl);
        when(restTemplate.exchange(eq(dummyUrl), eq(org.springframework.http.HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponseJson.toString()));

        List<ArticleDto> articles = theNewsApiFetcher.fetchArticles();

        assertNotNull(articles);
        assertEquals(1, articles.size());

        ArticleDto article = articles.get(0);
        assertEquals("AI Takes Over", article.getTitle());
        assertEquals("AI is now writing code.", article.getContent());
        assertEquals("http://example.com/ai", article.getUrl());
        assertEquals("TheNewsAPI", article.getSource());
        assertNotNull(article.getPublishedAt());

        verify(apiClientService).getApiClientByName("TheNewsAPI");
        verify(restTemplate).exchange(eq(dummyUrl), eq(HttpMethod.GET), isNull(), eq(String.class));
    }

    @Test
    void fetchArticles_InvalidPublishedAt_UsesNow() throws JSONException {
        JSONArray articleArray = new JSONArray()
                .put(new JSONObject()
                        .put("title", "News Without Time")
                        .put("description", "Something happened.")
                        .put("url", "http://example.com/news")
                        .put("source", "TheNewsAPI")
                        .put("published_at", "invalid-date"));

        JSONObject mockResponseJson = new JSONObject()
                .put("data", articleArray);

        when(apiClientService.getApiClientByName("TheNewsAPI")).thenReturn(dummyUrl);
        when(restTemplate.exchange(eq(dummyUrl), eq(HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockResponseJson.toString()));

        List<ArticleDto> articles = theNewsApiFetcher.fetchArticles();

        assertEquals(1, articles.size());
        assertEquals("News Without Time", articles.get(0).getTitle());
        assertNotNull(articles.get(0).getPublishedAt());
    }
}
