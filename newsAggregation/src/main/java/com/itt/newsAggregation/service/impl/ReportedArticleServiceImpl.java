package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.request.EmailRequest;
import com.itt.newsAggregation.dto.request.ReportRequestDto;
import com.itt.newsAggregation.dto.response.ReportResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.ReportedArticle;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.notification.NotificationSender;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.ReportedArticleRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.ReportedArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportedArticleServiceImpl implements ReportedArticleService {

    @Value("${report.threshold}")
    private int reportThreshold;

    private final ReportedArticleRepository reportedArticleRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final NotificationSender notificationSender;

    @Override
    public ReportResponseDto reportArticle(ReportRequestDto requestDto) {
        Article article = articleRepository.findById(requestDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (reportedArticleRepository.existsByUserIdAndArticleId(user.getId(), article.getId())) {
            throw new RuntimeException("You have already reported this article.");
        }

        ReportedArticle report = ReportedArticle.builder()
                .article(article)
                .user(user)
                .build();

        ReportedArticle savedReport = reportedArticleRepository.save(report);
        notifyAdmin(article.getId(), user);

        if (getReportCountForArticle(article.getId()) >= reportThreshold) {
            articleRepository.updateArticleHiddenStatus(article.getId(), true);
        }

        return ReportResponseDto.builder()
                .id(savedReport.getId())
                .articleId(savedReport.getArticle().getId())
                .userId(savedReport.getUser().getId())
                .reportedAt(savedReport.getReportedAt())
                .build();
    }

    private long getReportCountForArticle(Integer articleId) {
        return reportedArticleRepository.countByArticleId(articleId);
    }

    private void notifyAdmin(int articleId, User reporter) {
        userRepository.findByRole(User.Role.ADMIN)
                .stream()
                .findFirst()
                .ifPresent(admin -> {
                    EmailRequest emailRequest = EmailRequest.builder()
                            .to(admin.getEmail())
                            .subject("Article Reported by User")
                            .body("User ID: " + reporter.getId() +
                                    ", Username: " + reporter.getUsername() +
                                    " has reported Article ID: " + articleId + ".")
                            .build();

                    notificationSender.sendNotification(emailRequest);
                });
    }
}
