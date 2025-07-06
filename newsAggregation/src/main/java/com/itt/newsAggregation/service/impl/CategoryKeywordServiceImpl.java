package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.common.CategoryKeywordDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.CategoryKeyword;
import com.itt.newsAggregation.repository.CategoryKeywordRepository;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.service.CategoryKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryKeywordServiceImpl implements CategoryKeywordService {

    private final CategoryKeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryKeywordCacheService categoryKeywordCacheService;

    @Override
    public List<CategoryKeywordDto> getAll() {
        return keywordRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryKeywordDto addKeyword(CategoryKeywordDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        CategoryKeyword keyword = CategoryKeyword.builder()
                .category(category)
                .keyword(dto.getKeyword().toLowerCase())
                .build();

        keywordRepository.save(keyword);
        categoryKeywordCacheService.refreshCache();
        return toDto(keyword);
    }

    @Override
    public void deleteKeyword(Integer id) {
        keywordRepository.deleteById(id);
    }

    @Override
    public List<CategoryKeywordDto> getByCategoryId(Integer categoryId) {
        return keywordRepository.findAll().stream()
                .filter(k -> k.getCategory().getId().equals(categoryId))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CategoryKeywordDto toDto(CategoryKeyword keyword) {
        return CategoryKeywordDto.builder()
                .id(keyword.getId())
                .keyword(keyword.getKeyword())
                .categoryId(keyword.getCategory().getId())
                .categoryName(keyword.getCategory().getName())
                .build();
    }
}