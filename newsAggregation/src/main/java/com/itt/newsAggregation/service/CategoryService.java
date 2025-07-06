package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.request.CategoryRequestDto;
import com.itt.newsAggregation.dto.response.CategoryResponseDto;
import com.itt.newsAggregation.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryDto);
    CategoryResponseDto getCategoryById(Integer id);
    Optional<Category> findByName(String name);
    CategoryResponseDto getCategoryDetailsByName(String name);
    List<CategoryResponseDto> getAllCategories();
    boolean existsByName(String name);
}
