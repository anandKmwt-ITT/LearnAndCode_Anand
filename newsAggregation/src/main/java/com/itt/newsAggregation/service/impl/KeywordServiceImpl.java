package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.dto.KeywordDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.Keyword;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.KeywordRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordRepository keywordRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;

    @Override
    public KeywordDto createKeyword(KeywordDto dto) {
        // Validate user and category
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        // Check for duplicate
        if (keywordRepo.existsByNameAndUserIdAndCategoryId(dto.getName(), dto.getUserId(), dto.getCategoryId())) {
            throw new RuntimeException("Keyword already exists for this user and category.");
        }

        Keyword keyword = Keyword.builder()
                .name(dto.getName())
                .user(user)
                .category(category)
                .build();

        keyword = keywordRepo.save(keyword);

        return KeywordDto.builder()
                .id(keyword.getId())
                .name(keyword.getName())
                .userId(user.getId())
                .categoryId(category.getId())
                .build();
    }

    @Override
    public List<KeywordDto> getKeywordsByUser(Integer userId) {
        return keywordRepo.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<KeywordDto> getKeywordsByCategory(Integer categoryId) {
        return keywordRepo.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<KeywordDto> getKeywordsByUserAndCategory(Integer userId, Integer categoryId) {
        return keywordRepo.findByUserIdAndCategoryId(userId, categoryId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private KeywordDto mapToDto(Keyword k) {
        return KeywordDto.builder()
                .id(k.getId())
                .name(k.getName())
                .userId(k.getUser().getId())
                .categoryId(k.getCategory().getId())
                .build();
    }
}
