package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.request.CategoryRequestDto;
import com.itt.newsAggregation.dto.response.CategoryResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryKeywordCacheService categoryKeywordCacheService;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
        boolean exists = categoryRepository.existsByName(categoryDto.getName());
        if(exists) {
            throw new IllegalArgumentException("Category with name " + categoryDto.getName() + " already exists.");
        }
        Category savedCategory = categoryRepository.save(mapToCategory.apply(categoryDto));
        categoryKeywordCacheService.refreshCache();
        return mapToCategoryDto.apply(savedCategory);
    }

    @Override
    public CategoryResponseDto getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(mapToCategoryDto)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    @Override
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(mapToCategoryDto)
                .toList();
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public CategoryResponseDto getCategoryDetailsByName(String name) {
        Optional<Category> fetchedCategory = categoryRepository.findByName(name);
        if (!fetchedCategory.isPresent()){
            throw new ResourceNotFoundException("Category not found by name: "+ name);
        }
        return mapToCategoryDto.apply(fetchedCategory.get());
    }

    @Override
    public CategoryResponseDto toggleCategoryHiddenStatus(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));

        category.setHidden(!category.isHidden());
        Category updated = categoryRepository.save(category);

        return mapToCategoryDto.apply(updated);
    }


    private Function<Category, CategoryResponseDto> mapToCategoryDto = category -> CategoryResponseDto.builder()
            .id(category.getId())
            .name(category.getName())
            .hidden(category.isHidden())
            .build();

    private Function<CategoryRequestDto, Category> mapToCategory = categoryDto -> Category.builder()
            .name(categoryDto.getName())
            .build();

}
