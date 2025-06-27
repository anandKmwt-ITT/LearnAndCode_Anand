package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.UserReactionDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.model.UserReaction;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.UserReactionRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.UserReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserReactionServiceImpl implements UserReactionService {

    private final UserReactionRepository reactionRepo;
    private final UserRepository userRepo;
    private final ArticleRepository articleRepo;

    @Override
    public void addOrUpdateReaction(UserReactionDto dto) {
        User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Article article = articleRepo.findById(dto.getArticleId()).orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        Optional<UserReaction> existing = reactionRepo.findByUserIdAndArticleId(dto.getUserId(), dto.getArticleId());

        if (existing.isPresent()) {
            UserReaction reaction = existing.get();
            reaction.setReaction(dto.getReaction());
            reactionRepo.save(reaction);
        } else {
            UserReaction newReaction = UserReaction.builder()
                    .user(user)
                    .article(article)
                    .reaction(dto.getReaction())
                    .build();
            reactionRepo.save(newReaction);
        }
    }

    @Override
    public void removeReaction(UserReactionDto dto) {
        reactionRepo.deleteByUserIdAndArticleId(dto.getUserId(), dto.getArticleId());
    }

    @Override
    public Map<String, Long> getReactionCountsForArticle(Integer articleId) {
        long likes = reactionRepo.countByArticleIdAndReaction(articleId, UserReaction.ReactionType.LIKE);
        long dislikes = reactionRepo.countByArticleIdAndReaction(articleId, UserReaction.ReactionType.DISLIKE);

        Map<String, Long> result = new HashMap<>();
        result.put("likes", likes);
        result.put("dislikes", dislikes);
        return result;
    }

    @Override
    public List<UserReactionDto> getUserReactions(Integer userId) {
        List<UserReaction> reactions = reactionRepo.findByUserId(userId);
        List<UserReactionDto> dtos = new ArrayList<>();

        for (UserReaction r : reactions) {
            dtos.add(UserReactionDto.builder()
                    .userId(r.getUser().getId())
                    .articleId(r.getArticle().getId())
                    .reaction(r.getReaction())
                    .build());
        }
        return dtos;
    }
}
