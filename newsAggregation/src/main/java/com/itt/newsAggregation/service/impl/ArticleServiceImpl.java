package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.dto.common.SavedArticleDto;
import com.itt.newsAggregation.dto.common.UserReactionDto;
import com.itt.newsAggregation.dto.request.CategoryRequestDto;
import com.itt.newsAggregation.dto.response.NewsHeadlineResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.*;
import com.itt.newsAggregation.notification.NotificationDispatcher;
import com.itt.newsAggregation.repository.*;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.CategoryService;
import com.itt.newsAggregation.service.UserReactionService;
import com.itt.newsAggregation.service.UserReadArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SavedArticleRepository savedArticleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Autowired
    private UserReactionService userReactionService;

    @Autowired
    private UserReadArticleService userReadArticleService;

    @Autowired
    private UserReadArticleRepository userReadArticleRepository;

    @Autowired
    private CategoryKeywordCacheService keywordCacheService;

    @Override
    public void saveArticles(List<ArticleDto> articles) {

        List<Article> newArticles = new ArrayList<>();
        for (ArticleDto dto : articles) {
            if (!articleRepository.existsByUrl(dto.getUrl())) {
                String categoryName = assignCategory(dto);
                if (!categoryService.existsByName(categoryName)) {
                    CategoryRequestDto categoryDto = CategoryRequestDto.builder().name(categoryName).build();
                    categoryService.createCategory(categoryDto);
                }
                Category category = categoryService.findByName(categoryName).get();
                Article article = mapToArticle.apply(dto);
                article.setCategory(category);
                newArticles.add(article);
            }
        }
        if (!newArticles.isEmpty()) {
            List<Article> savedArticles = articleRepository.saveAll(newArticles);
            notificationDispatcher.notifyUsers(savedArticles);
        }
    }

    @Override
    public List<NewsHeadlineResponseDto> getHeadlines(Integer categoryId, String startDateStr, String endDateStr) {
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : LocalDate.MIN;
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Article> articles;

        if (categoryId != null) {
            articles = articleRepository.findByCategoryIdAndPublishedAtBetween(categoryId, startDateTime, endDateTime);
        } else {
            articles = articleRepository.findByPublishedAtBetween(startDateTime, endDateTime);
        }

        return articles.stream()
                .map(article -> NewsHeadlineResponseDto.builder()
                        .id(article.getId())
                        .title(article.getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<NewsHeadlineResponseDto> getAllHeadlines() {
        return articleRepository.findAll().stream().map(article -> NewsHeadlineResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .build()).collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> getArticlesByDateRange(String startDateStr, String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;

        if (startDateStr == null && endDateStr == null) {
            startDate = LocalDate.now();
            endDate = startDate;
        } else {
            startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : LocalDate.MIN;
            endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusSeconds(1); // end of day

        List<Article> articles = articleRepository.findByPublishedAtBetween(startDateTime, endDateTime);

        return articles.stream()
                .map(mapToArticleDto)
                .collect(Collectors.toList());
    }

    @Override
    public SavedArticleDto saveArticle(Integer userId, Integer articleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Article> articleOpt = articleRepository.findById(articleId);

        if (userOpt.isPresent() && articleOpt.isPresent()) {
            SavedArticle savedArticle = SavedArticle.builder()
                    .user(userOpt.get())
                    .article(articleOpt.get())
                    .savedAt(LocalDateTime.now())
                    .build();
            SavedArticle savedArticle1 = savedArticleRepository.save(savedArticle);

            return mapToSavedArticleDto.apply(savedArticle1);
        }
        throw new IllegalArgumentException("User or Article not found");
    }

    @Override
    public List<ArticleDto> getSavedArticles(String username) {
        List<SavedArticle> savedArticles = savedArticleRepository.findByUserUsername(username);
        List<ArticleDto> savedArticleDtos = new ArrayList<>();
        for (SavedArticle savedArticle : savedArticles) {
            ArticleDto articleById = getArticleById(savedArticle.getArticle().getId());
            if(articleById != null){
                savedArticleDtos.add(articleById);
            }
        }
        return savedArticleDtos;
    }

    @Override
    public ArticleDto readArticle(Integer userId, Integer articleId) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        if (articleOpt.isPresent()) {
            userReadArticleService.markAsRead(userId, articleId);
            return mapToArticleDto.apply(articleOpt.get());
        }
        throw new ResourceNotFoundException("Article not found with id: " + articleId);
    }

    public List<ArticleDto> getLikedArticles(Integer userId) {
        List<UserReactionDto> userReactions = userReactionService.getUserReactions(userId);

        return userReactions.stream()
                .filter(reaction -> reaction.getReaction() == UserReaction.ReactionType.LIKE)
                .map(reaction -> getArticleById(reaction.getArticleId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> getViewedArticles(Integer userId) {
        return userReadArticleRepository.findByUserId(userId).stream()
                .map(UserReadArticle::getArticle)
                .map(article -> getArticleById(article.getId()))
                .toList();
    }

    @Override
    public ArticleDto getArticleById(Integer id) {
        Optional<Article> articleOpt = articleRepository.findById(id);
        if (articleOpt.isPresent()) {
            return mapToArticleDto.apply(articleOpt.get());
        }
        throw new ResourceNotFoundException("Article not found with id: " + id);
    }

    @Override
    public List<ArticleDto> searchArticles(String keyword) {
        List<Article> articles = articleRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
        return articles.stream()
                .map(mapToArticleDto)
                .collect(Collectors.toList());
    }

    private String assignCategory(ArticleDto dto) {
        String text = (dto.getTitle() + " " + dto.getContent()).toLowerCase();
        return keywordCacheService.getCategoryForText(text);
    }

    public final Function<Article, ArticleDto> mapToArticleDto = article -> ArticleDto.builder()
            .id(article.getId())
            .title(article.getTitle())
            .content(article.getContent())
            .source(article.getSource())
            .url(article.getUrl())
            .category(article.getCategory().getName())
            .publishedAt(article.getPublishedAt())
            .build();

    public final Function<ArticleDto, Article> mapToArticle = dto -> Article.builder()
            .title(dto.getTitle())
            .content(dto.getContent())
            .source(dto.getSource())
            .url(dto.getUrl())
            .publishedAt(dto.getPublishedAt())
            .createdAt(LocalDateTime.now())
            .build();

    public final Function<SavedArticle, SavedArticleDto> mapToSavedArticleDto = savedArticle -> SavedArticleDto.builder()
            .userId(savedArticle.getUser().getId())
            .articleId(savedArticle.getArticle().getId())
            .savedAt(savedArticle.getSavedAt())
            .build();
}