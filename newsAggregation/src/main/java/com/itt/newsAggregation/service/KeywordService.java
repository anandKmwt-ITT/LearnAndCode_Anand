package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.KeywordDto;

import java.util.List;

public interface KeywordService {
    KeywordDto createKeyword(KeywordDto dto);
    List<KeywordDto> getKeywordsByUser(Integer userId);
    List<KeywordDto> getKeywordsByCategory(Integer categoryId);
    List<KeywordDto> getKeywordsByUserAndCategory(Integer userId, Integer categoryId);
}
