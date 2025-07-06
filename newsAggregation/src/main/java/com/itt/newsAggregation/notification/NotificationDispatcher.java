package com.itt.newsAggregation.notification;

import com.itt.newsAggregation.dto.request.EmailRequest;
import com.itt.newsAggregation.dto.common.NotificationPreferenceDto;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.UserRepository;
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

    public void notifyUsers(List<Article> articles) {
        if (articles.isEmpty()) {
            log.info("No new articles to notify users about.");
            return;
        }

        List<NotificationPreferenceDto> allPrefs = notificationPreferenceService.getAllNotificationPreferences();

        Map<String, List<String>> articlesByCategory = articles.stream()
                .collect(Collectors.groupingBy(
                        art -> art.getCategory().getName(),
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

            String message = "Hello " + user.getUsername() + ",\n\n" +
                    "Here are the new articles in the " + category + " category:\n" +
                    String.join("\n", urls) +
                    "\n\nBest regards,\nNews Aggregation Team";

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
}
