package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.ArticleDto;
import com.itt.newsAggregation.dto.NewsHeadlineResponseDto;
import com.itt.newsAggregation.dto.SavedArticleDto;

import java.util.List;

public interface ArticleService {
    void saveArticles(List<ArticleDto> articles);
    List<ArticleDto> getAllArticles();
    SavedArticleDto saveArticle(Integer userId, Integer articleId);
    List<SavedArticleDto> getSavedArticles(String username);
    List<NewsHeadlineResponseDto> getAllHeadlines();
    ArticleDto getArticleById(Integer id);
    List<ArticleDto> searchArticles(String keyword);
}
