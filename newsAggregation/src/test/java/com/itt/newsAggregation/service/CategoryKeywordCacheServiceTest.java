package com.itt.newsAggregation.service;

import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.CategoryKeyword;
import com.itt.newsAggregation.repository.CategoryKeywordRepository;
import com.itt.newsAggregation.service.impl.CategoryKeywordCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryKeywordCacheServiceTest {

    @Mock
    private CategoryKeywordRepository keywordRepository;

    @InjectMocks
    private CategoryKeywordCacheService cacheService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadKeywordsIntoCache_ShouldGroupKeywordsByCategory() {
        Category tech = Category.builder().id(1).name("Technology").build();
        Category sports = Category.builder().id(2).name("Sports").build();

        List<CategoryKeyword> mockKeywords = List.of(
                CategoryKeyword.builder().category(tech).keyword("AI").build(),
                CategoryKeyword.builder().category(tech).keyword("Machine Learning").build(),
                CategoryKeyword.builder().category(sports).keyword("Cricket").build()
        );

        when(keywordRepository.findAll()).thenReturn(mockKeywords);

        cacheService.loadKeywordsIntoCache();

        assertEquals("Technology", cacheService.getCategoryForText("The future of AI"));
        assertEquals("Technology", cacheService.getCategoryForText("Advancements in machine learning"));
        assertEquals("Sports", cacheService.getCategoryForText("Cricket world cup"));
        assertEquals("General", cacheService.getCategoryForText("No matching keyword"));
    }

    @Test
    void getCategoryForText_ShouldReturnGeneralIfNoMatchFound() {
        when(keywordRepository.findAll()).thenReturn(List.of());
        cacheService.loadKeywordsIntoCache();

        String result = cacheService.getCategoryForText("This is a random article");
        assertEquals("General", result);
    }

    @Test
    void refreshCache_ShouldReloadKeywordsFromRepository() {
        when(keywordRepository.findAll()).thenReturn(List.of(
                CategoryKeyword.builder()
                        .category(Category.builder().name("Finance").build())
                        .keyword("Stocks")
                        .build()
        ));

        cacheService.refreshCache();

        String result = cacheService.getCategoryForText("Investing in stocks");
        assertEquals("Finance", result);
    }
}
