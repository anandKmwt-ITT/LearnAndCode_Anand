package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.ArticleDto;

import java.util.List;

public interface NewsFetcher {
    public List<ArticleDto> fetchArticles();
}
