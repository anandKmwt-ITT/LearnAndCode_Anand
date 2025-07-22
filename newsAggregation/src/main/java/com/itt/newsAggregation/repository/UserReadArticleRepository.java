package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.UserReadArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReadArticleRepository extends JpaRepository<UserReadArticle, Integer> {
    List<UserReadArticle> findByUserId(Integer userId);
    boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
}
