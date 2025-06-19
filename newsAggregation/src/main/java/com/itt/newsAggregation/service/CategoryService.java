package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.CategoryDto;
import com.itt.newsAggregation.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(Integer id);
    Optional<Category> findByName(String name);
    List<CategoryDto> getAllCategories();
    boolean existsByName(String name);
}
