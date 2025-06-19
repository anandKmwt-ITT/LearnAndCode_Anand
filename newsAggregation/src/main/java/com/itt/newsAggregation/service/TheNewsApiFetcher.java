package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.ArticleDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class TheNewsApiFetcher implements NewsFetcher {

    private static final String CLIENT_NAME = "TheNewsAPI";

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ArticleDto> fetchArticles() {
        String url = apiClientService.getApiClientByName(CLIENT_NAME);
        String response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();

        JSONObject json = new JSONObject(response);
        JSONArray articles = json.getJSONArray("data");

        List<ArticleDto> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        for (int i = 0; i < articles.length(); i++) {
            JSONObject obj = articles.getJSONObject(i);

            LocalDateTime publishedAt = null;
            try {
                publishedAt = LocalDateTime.parse(obj.optString("published_at", ""), formatter);
            } catch (Exception e) {
                publishedAt = LocalDateTime.now(); // fallback if parsing fails
            }

            list.add(ArticleDto.builder()
                    .title(obj.optString("title", "Untitled"))
                    .content(obj.optString("description", ""))
                    .url(obj.optString("url", ""))
                    .source(obj.optString("source", "Unknown"))
                    .publishedAt(publishedAt)
                    .build());
        }

        return list;
    }
}
