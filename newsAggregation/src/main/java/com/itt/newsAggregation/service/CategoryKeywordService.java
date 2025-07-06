package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.CategoryKeywordDto;

import java.util.List;

public interface CategoryKeywordService {
    List<CategoryKeywordDto> getAll();
    CategoryKeywordDto addKeyword(CategoryKeywordDto dto);
    void deleteKeyword(Integer id);
    List<CategoryKeywordDto> getByCategoryId(Integer categoryId);
}
