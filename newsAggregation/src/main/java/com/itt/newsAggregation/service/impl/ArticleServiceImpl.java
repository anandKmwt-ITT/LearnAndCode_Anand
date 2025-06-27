package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.*;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.SavedArticle;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.notification.EmailService;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.SavedArticleRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.CategoryService;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public List<NewsHeadlineResponseDto> getAllHeadlines()
    {
        List<NewsHeadlineResponseDto> headlines = articleRepository.findAll().stream().map(article -> NewsHeadlineResponseDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .build()).collect(Collectors.toList());
        return headlines;
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
    public List<SavedArticleDto> getSavedArticles(String username) {
        List<SavedArticle> savedArticles = savedArticleRepository.findByUserUsername(username);
        List<SavedArticleDto> savedArticleDtos = new ArrayList<>();
        for (SavedArticle savedArticle : savedArticles) {
            savedArticleDtos.add(mapToSavedArticleDto.apply(savedArticle));
        }
        return savedArticleDtos;
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

    if (text.contains("sport") || text.contains("football")) return "Sports";
    if (text.contains("business") || text.contains("market")) return "Business";
    if (text.contains("movie") || text.contains("music")) return "Entertainment";
    if (text.contains("tech") || text.contains("ai")) return "Technology";
    return "General";
    }

    @Async
    private void notifyUsers(List<Article> articles) {
        if (articles.isEmpty()) {
            log.info("No new articles to notify users about.");
            return;
        }

        List<NotificationPreferenceDto> allPrefs = notificationPreferenceService.getAllNotificationPreferences();

        Map<String, List<String>> articlesByCategory = articles.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getCategory().getName(),
                        Collectors.mapping(Article::getUrl, Collectors.toList())
                ));

        for (NotificationPreferenceDto pref : allPrefs) {
            if (!pref.getIsEnabled()) continue;

            String username = pref.getUsername();
            String category = pref.getCategory();
            List<String> urls = articlesByCategory.getOrDefault(category, List.of());

            if (urls.isEmpty()) continue;

            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) continue;

            User user = userOpt.get();
            log.info("Sending notification to user: {}", username);

            emailService.sendEmail(
                    EmailRequest.builder()
                            .to(user.getEmail())
                            .subject("New Articles in " + category)
                            .body("Hello " + user.getUsername() + ",\n\n" +
                                    "Here are the new articles in the " + category + " category:\n" +
                                    String.join("\n", urls) +
                                    "\n\nBest regards,\nNews Aggregation Team")
                            .build()
            );
        }
    }

    private final Function<Article, ArticleDto> mapToArticleDto = article -> ArticleDto.builder()
            .id(article.getId())
            .title(article.getTitle())
            .content(article.getContent())
            .source(article.getSource())
            .url(article.getUrl())
            .category(article.getCategory().getName())
            .publishedAt(article.getPublishedAt())
            .build();

    private final Function<SavedArticle, SavedArticleDto> mapToSavedArticleDto = savedArticle -> SavedArticleDto.builder()
            .userId(savedArticle.getUser().getId())
            .articleId(savedArticle.getArticle().getId())
            .savedAt(savedArticle.getSavedAt())
            .build();


}

