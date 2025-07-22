package com.itt.newsAggregation.repository;

import com.itt.newsAggregation.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);
    Optional<Category> findByName(String name);
    @Query("SELECT c.hidden FROM Category c WHERE c.name = :name")
    Boolean isHiddenByName(@Param("name") String name);
}
