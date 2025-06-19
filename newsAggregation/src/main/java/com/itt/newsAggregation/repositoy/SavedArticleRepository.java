package com.itt.newsAggregation.repositoy;

import com.itt.newsAggregation.model.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Integer> {
    boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
    void deleteByUserIdAndArticleId(Integer userId, Integer articleId);
    List<SavedArticle> findByUserId(Integer userId);
}
