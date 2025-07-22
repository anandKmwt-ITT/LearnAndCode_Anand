package com.itt.newsAggregation.external;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.service.ApiClientService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class NewsApiFetcher implements NewsFetcher {

    private static final String CLIENT_NAME = "NewsAPI";

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private RestTemplate restTemplate;

    public List<ArticleDto> fetchArticles() {

        String url = apiClientService.getApiClientByName(CLIENT_NAME);

        String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();

        JSONObject json = new JSONObject(response);
        JSONArray articles = json.getJSONArray("articles");

        List<ArticleDto> list = new ArrayList<>();

        for (int i = 0; i < articles.length(); i++) {
            JSONObject obj = articles.getJSONObject(i);
            list.add(ArticleDto.builder()
                    .title(obj.optString("title", "Untitled"))
                    .content(obj.optString("content", ""))
                    .url(obj.optString("url", ""))
                    .source(obj.optJSONObject("source").optString("name", "Unknown"))
                    .publishedAt(LocalDateTime.now())
                    .build());
        }

        return list;
    }
}