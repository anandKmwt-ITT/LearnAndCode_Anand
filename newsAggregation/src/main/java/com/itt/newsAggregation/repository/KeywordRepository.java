package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    List<Keyword> findByUserId(Integer userId);
    List<Keyword> findByCategoryId(Integer categoryId);
    List<Keyword> findByUserIdAndCategoryId(Integer userId, Integer categoryId);
    boolean existsByNameAndUserIdAndCategoryId(String name, Integer userId, Integer categoryId);
}
