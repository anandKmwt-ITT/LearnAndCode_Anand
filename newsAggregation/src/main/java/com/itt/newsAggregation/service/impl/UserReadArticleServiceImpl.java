package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.model.UserReadArticle;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.UserReadArticleRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.UserReadArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReadArticleServiceImpl implements UserReadArticleService {

    private final UserReadArticleRepository readRepo;
    private final UserRepository userRepo;
    private final ArticleRepository articleRepo;

    @Override
    public void markAsRead(Integer userId, Integer articleId) {
        boolean alreadyExists = readRepo.existsByUserIdAndArticleId(userId, articleId);
        if (alreadyExists) return;

        User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Article article = articleRepo.findById(articleId).orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        UserReadArticle readEntry = UserReadArticle.builder()
                .user(user)
                .article(article)
                .build();

        readRepo.save(readEntry);
    }

}
