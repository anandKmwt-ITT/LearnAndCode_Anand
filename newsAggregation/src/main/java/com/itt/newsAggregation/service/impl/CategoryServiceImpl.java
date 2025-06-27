package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.CategoryDto;
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

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        boolean exists = categoryRepository.existsByName(categoryDto.getName());
        if(exists) {
            throw new IllegalArgumentException("Category with name " + categoryDto.getName() + " already exists.");
        }
        Category category = mapToCategory.apply(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryDto.apply(savedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(mapToCategoryDto)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
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

    private Function<Category, CategoryDto> mapToCategoryDto = category -> CategoryDto.builder()
            .name(category.getName())
            .build();

    private Function<CategoryDto, Category> mapToCategory = categoryDto -> Category.builder()
            .name(categoryDto.getName())
            .build();

}
