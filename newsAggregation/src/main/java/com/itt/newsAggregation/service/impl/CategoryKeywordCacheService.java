package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.model.CategoryKeyword;
import com.itt.newsAggregation.repository.CategoryKeywordRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryKeywordCacheService {

    private final CategoryKeywordRepository keywordRepository;

    private Map<String, List<String>> categoryKeywords = new HashMap<>();

    @PostConstruct
    public void loadKeywordsIntoCache() {
        List<CategoryKeyword> allKeywords = keywordRepository.findAll();

        categoryKeywords = allKeywords.stream()
                .collect(Collectors.groupingBy(
                        k -> k.getCategory().getName(),
                        Collectors.mapping(k -> k.getKeyword().toLowerCase(), Collectors.toList())
                ));
    }

    public String getCategoryForText(String text) {
        String lowerText = text.toLowerCase();

        for (Map.Entry<String, List<String>> entry : categoryKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lowerText.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }

        return "General";
    }

    public void refreshCache() {
        loadKeywordsIntoCache();
    }
}
