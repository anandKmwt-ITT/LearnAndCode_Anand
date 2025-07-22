package com.itt.newsAggregation.external;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.service.ApiClientService;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsApiFetcherTest {

    @InjectMocks
    private NewsApiFetcher newsApiFetcher;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private RestTemplate restTemplate;

    private final String fakeUrl = "http://newsapi.org/v2/top-headlines?apiKey=XYZ";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchArticles_ReturnsListOfArticles() throws JSONException {
        String mockJsonResponse = new JSONObject()
                .put("articles", new JSONArray()
                        .put(new JSONObject()
                                .put("title", "Sample Title")
                                .put("content", "Sample Content")
                                .put("url", "http://example.com")
                                .put("source", new JSONObject().put("name", "Sample Source"))
                        )
                ).toString();

        when(apiClientService.getApiClientByName("NewsAPI")).thenReturn(fakeUrl);
        when(restTemplate.exchange(eq(fakeUrl), eq(org.springframework.http.HttpMethod.GET), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok(mockJsonResponse));

        List<ArticleDto> articles = newsApiFetcher.fetchArticles();

        assertNotNull(articles);
        assertEquals(1, articles.size());
        assertEquals("Sample Title", articles.get(0).getTitle());
        assertEquals("Sample Content", articles.get(0).getContent());
        assertEquals("http://example.com", articles.get(0).getUrl());
        assertEquals("Sample Source", articles.get(0).getSource());

        verify(apiClientService).getApiClientByName("NewsAPI");
        verify(restTemplate).exchange(eq(fakeUrl), eq(org.springframework.http.HttpMethod.GET), isNull(), eq(String.class));
    }
}
