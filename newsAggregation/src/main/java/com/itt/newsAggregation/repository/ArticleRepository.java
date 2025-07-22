package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    boolean existsByUrl(String url);
    Optional<Article> findById(Integer id);
    List<Article> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);
    List<Article> findByCategoryId(Integer categoryId);
    List<Article> findByPublishedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Article> findByCategoryIdAndPublishedAtBetween(Integer categoryId, LocalDateTime start, LocalDateTime end);
    @Modifying
    @Transactional
    @Query(value = "UPDATE article SET is_hidden = :hidden WHERE id = :articleId", nativeQuery = true)
    void updateArticleHiddenStatus(Integer articleId, boolean hidden);
}
