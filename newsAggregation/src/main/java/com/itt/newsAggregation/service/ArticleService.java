package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.dto.response.NewsHeadlineResponseDto;
import com.itt.newsAggregation.dto.common.SavedArticleDto;

import java.util.List;

public interface ArticleService {
    void saveArticles(List<ArticleDto> articles);
    List<NewsHeadlineResponseDto> getHeadlines(Integer categoryId, String startDateStr, String endDateStr);
    SavedArticleDto saveArticle(Integer userId, Integer articleId);
    List<ArticleDto> getSavedArticles(String username);
    List<NewsHeadlineResponseDto> getAllHeadlines();
    ArticleDto getArticleById(Integer id);
    ArticleDto readArticle(Integer userId, Integer articleId);
    List<ArticleDto> searchArticles(String keyword);
    List<ArticleDto> getArticlesByDateRange(String startDate, String endDate);
    List<ArticleDto> getLikedArticles(Integer userId);
    List<ArticleDto> getViewedArticles(Integer userId);
    String updateArticleHiddenStatus(Integer articleId, boolean hidden);
    String toggleArticlesByKeyword(String keyword, boolean hidden);
}
