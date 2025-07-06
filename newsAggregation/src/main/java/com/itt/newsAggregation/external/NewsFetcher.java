package com.itt.newsAggregation.external;

import com.itt.newsAggregation.dto.common.ArticleDto;

import java.util.List;

public interface NewsFetcher {
    public List<ArticleDto> fetchArticles();
}
