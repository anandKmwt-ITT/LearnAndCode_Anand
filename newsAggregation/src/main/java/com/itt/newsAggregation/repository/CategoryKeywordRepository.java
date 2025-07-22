package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.CategoryKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryKeywordRepository extends JpaRepository<CategoryKeyword, Integer> {
    List<CategoryKeyword> findAll();
}
