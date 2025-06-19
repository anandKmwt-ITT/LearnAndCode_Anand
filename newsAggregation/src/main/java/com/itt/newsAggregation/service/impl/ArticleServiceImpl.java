package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.*;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.SavedArticle;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.notification.EmailService;
import com.itt.newsAggregation.repositoy.ArticleRepository;
import com.itt.newsAggregation.repositoy.SavedArticleRepository;
import com.itt.newsAggregation.repositoy.UserRepository;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.CategoryService;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
    private NotificationPreferenceService notificationPreferenceService;

    @Autowired
    private EmailService emailService;


    @Override
    public void saveArticles(List<ArticleDto> articles) {
        List<Article> newArticles = new ArrayList<>();
        for (ArticleDto dto : articles) {
            if (!articleRepository.existsByUrl(dto.getUrl())) {
                String categoryName = assignCategory(dto);
                if( !categoryService.existsByName(categoryName)) {
                    CategoryDto categoryDto = CategoryDto.builder().name(categoryName).build();
                    categoryService.createCategory(categoryDto);
                }
                Category category = categoryService.findByName(categoryName).get();

                Article article = Article.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .source(dto.getSource())
                        .url(dto.getUrl())
                        .category(category)
                        .publishedAt(dto.getPublishedAt())
                        .createdAt(LocalDateTime.now())
                        .build();
                newArticles.add(article);
            }
        }
        if (!newArticles.isEmpty()) {
            List<Article> savedArticles = articleRepository.saveAll(newArticles);
            notifyUsers(savedArticles);
        }
    }

    @Override
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> articleDtos = new ArrayList<>();
        for (Article article : articles) {
            articleDtos.add(mapToArticleDto.apply(article));
        }
        return articleDtos;
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
    public List<SavedArticleDto> getSavedArticles(Integer userId) {
        List<SavedArticle> savedArticles = savedArticleRepository.findByUserId(userId);
        List<SavedArticleDto> savedArticleDtos = new ArrayList<>();
        for (SavedArticle savedArticle : savedArticles) {
            savedArticleDtos.add(mapToSavedArticleDto.apply(savedArticle));
        }
        return savedArticleDtos;
    }

    private String assignCategory(ArticleDto dto) {
    String text = (dto.getTitle() + " " + dto.getContent()).toLowerCase();

    if (text.contains("sport") || text.contains("football")) return "Sports";
    if (text.contains("business") || text.contains("market")) return "Business";
    if (text.contains("movie") || text.contains("music")) return "Entertainment";
    if (text.contains("tech") || text.contains("ai")) return "Technology";
    return "General";
    }

    private final Function<Article, ArticleDto> mapToArticleDto = article -> ArticleDto.builder()
            .id(article.getId())
            .title(article.getTitle())
            .content(article.getContent())
            .source(article.getSource())
            .url(article.getUrl())
            .publishedAt(article.getPublishedAt())
            .build();

    private final Function<SavedArticle, SavedArticleDto> mapToSavedArticleDto = savedArticle -> SavedArticleDto.builder()
            .userId(savedArticle.getUser().getId())
            .articleId(savedArticle.getArticle().getId())
            .savedAt(savedArticle.getSavedAt())
            .build();

    private void notifyUsers(List<Article> articles) {
        if (articles.isEmpty()) {
            log.info("No new articles to notify users about.");
            return;
        }
        List<NotificationPreferenceDto> allNotificationPreferences = notificationPreferenceService.getAllNotificationPreferences();

        for( NotificationPreferenceDto preference : allNotificationPreferences) {
            String category = preference.getCategory();
             List<String> filteredArticles = articles.stream()
                    .filter(article -> article.getCategory().getName().equalsIgnoreCase(category))
                    .map(Article::getUrl).toList();

            List<String> usernames = notificationPreferenceService.getAllNotificationPreferences().stream()
                    .filter(pref -> pref.getUsername().equalsIgnoreCase(category) && pref.getIsEnabled().equals(true))
                    .map(NotificationPreferenceDto::getUsername).toList();

            for( String username : usernames) {
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    emailService.sendEmail(EmailRequest.builder().to(user.getEmail())
                            .subject("New Articles in " + category)
                            .body("Hello " + user.getUsername() + ",\n\n" +
                                    "Here are the new articles in the " + category + " category:\n" +
                                    String.join("\n", filteredArticles) +
                                    "\n\nBest regards,\nNews Aggregation Team")
                            .build());
                } else {
                    log.warn("User not found: {}", username);
                }
            }

        }

    }
}

