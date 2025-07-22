package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.common.CategoryKeywordDto;
import com.itt.newsAggregation.service.CategoryKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category-keywords")
@RequiredArgsConstructor
public class CategoryKeywordController {

    private final CategoryKeywordService keywordService;

    @GetMapping
    public ResponseEntity<List<CategoryKeywordDto>> getAllKeywords() {
        return ResponseEntity.ok(keywordService.getAll());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategoryKeywordDto>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(keywordService.getByCategoryId(categoryId));
    }

    @PostMapping
    public ResponseEntity<CategoryKeywordDto> addKeyword(@RequestBody CategoryKeywordDto dto) {
        return ResponseEntity.ok(keywordService.addKeyword(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKeyword(@PathVariable Integer id) {
        keywordService.deleteKeyword(id);
        return ResponseEntity.noContent().build();
    }
}
