package com.itt.newsAggregation.repositoy;

import com.itt.newsAggregation.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    boolean existsByUrl(String url);
    Optional<Article> findById(Integer id);
}
