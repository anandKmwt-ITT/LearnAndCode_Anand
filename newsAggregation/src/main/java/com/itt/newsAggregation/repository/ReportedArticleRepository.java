package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.ReportedArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedArticleRepository extends JpaRepository<ReportedArticle, Integer> {
    boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
    long countByArticleId(int articleId);
}
