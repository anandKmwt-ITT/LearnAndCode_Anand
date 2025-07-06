package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.common.KeywordDto;
import com.itt.newsAggregation.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keywords")
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    @PostMapping
    public ResponseEntity<KeywordDto> createKeyword(@RequestBody KeywordDto dto) {
        KeywordDto created = keywordService.createKeyword(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<KeywordDto>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(keywordService.getKeywordsByUser(userId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<KeywordDto>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(keywordService.getKeywordsByCategory(categoryId));
    }

    @GetMapping("/user/{userId}/category/{categoryId}")
    public ResponseEntity<List<KeywordDto>> getByUserAndCategory(
            @PathVariable Integer userId,
            @PathVariable Integer categoryId
    ) {
        return ResponseEntity.ok(keywordService.getKeywordsByUserAndCategory(userId, categoryId));
    }
}