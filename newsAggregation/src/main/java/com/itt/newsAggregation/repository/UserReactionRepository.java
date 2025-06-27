package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReactionRepository extends JpaRepository<UserReaction, Integer> {
    Optional<UserReaction> findByUserIdAndArticleId(Integer userId, Integer articleId);
    Long countByArticleIdAndReaction(Integer articleId, UserReaction.ReactionType reaction);
    List<UserReaction> findByUserId(Integer userId);
    void deleteByUserIdAndArticleId(Integer userId, Integer articleId);
}
