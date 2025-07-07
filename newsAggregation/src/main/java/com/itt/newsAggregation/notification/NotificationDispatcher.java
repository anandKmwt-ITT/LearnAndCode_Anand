package com.itt.newsAggregation.notification;

import com.itt.newsAggregation.dto.common.KeywordDto;
import com.itt.newsAggregation.dto.request.EmailRequest;
import com.itt.newsAggregation.dto.common.NotificationPreferenceDto;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.KeywordService;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import com.itt.newsAggregation.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NotificationDispatcher {

    @Autowired
    private NotificationSender notificationSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationPreferenceService notificationPreferenceService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private KeywordService keywordService;

    public void notifyUsers(List<Article> articles) {
        if (articles.isEmpty()) {
            log.info("No new articles to notify users about.");
            return;
        }

        Map<String, List<String>> articlesByCategory = groupArticlesByCategory(articles);
        List<NotificationPreferenceDto> allPrefs = notificationPreferenceService.getAllNotificationPreferences();

        for (NotificationPreferenceDto pref : allPrefs) {
            if (!Boolean.TRUE.equals(pref.getIsEnabled())) continue;

            Optional<User> userOpt = findUser(pref.getUsername());
            if (userOpt.isEmpty()) continue;

            User user = userOpt.get();
            List<String> urls = articlesByCategory.getOrDefault(pref.getCategory(), List.of());
            if (urls.isEmpty()) continue;

            Integer categoryId = getCategoryId(articles, pref.getCategory());
            List<String> keywords = getUserKeywords(user.getId(), categoryId);

            String message = buildNotificationMessage(user.getUsername(), pref.getCategory(), urls, keywords);
            sendAndSaveNotification(user, pref.getCategory(), message);
        }
    }

    private Map<String, List<String>> groupArticlesByCategory(List<Article> articles) {
        return articles.stream().collect(Collectors.groupingBy(
                art -> art.getCategory().getName(),
                Collectors.mapping(Article::getUrl, Collectors.toList())
        ));
    }

    private Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    private Integer getCategoryId(List<Article> articles, String categoryName) {
        return articles.stream()
                .filter(a -> a.getCategory().getName().equalsIgnoreCase(categoryName))
                .map(a -> a.getCategory().getId())
                .findFirst()
                .orElse(null);
    }

    private List<String> getUserKeywords(Integer userId, Integer categoryId) {
        if (categoryId == null) return List.of();
        return keywordService.getKeywordsByUserAndCategory(userId, categoryId)
                .stream()
                .map(KeywordDto::getName)
                .collect(Collectors.toList());
    }

    private String buildNotificationMessage(String username, String category, List<String> urls, List<String> keywords) {
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(username).append(",\n\n")
                .append("Here are the new articles in the ").append(category).append(" category:\n")
                .append(String.join("\n", urls));

        if (!keywords.isEmpty()) {
            message.append("\n\nYour configured keywords for this category:\n")
                    .append(String.join(", ", keywords));
        }

        message.append("\n\nBest regards,\nNews Aggregation Team");
        return message.toString();
    }

    private void sendAndSaveNotification(User user, String category, String message) {
        log.info("Sending notification to user: {}", user.getUsername());

        notificationSender.sendNotification(
                EmailRequest.builder()
                        .to(user.getEmail())
                        .subject("New Articles in " + category)
                        .body(message)
                        .build()
        );

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .build();

        notificationService.saveNotification(notification);
    }
}