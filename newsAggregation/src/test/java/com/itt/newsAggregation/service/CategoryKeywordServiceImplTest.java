package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.CategoryKeywordDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.CategoryKeyword;
import com.itt.newsAggregation.repository.CategoryKeywordRepository;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.service.impl.CategoryKeywordCacheService;
import com.itt.newsAggregation.service.impl.CategoryKeywordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryKeywordServiceImplTest {

    @Mock
    private CategoryKeywordRepository keywordRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private CategoryKeywordCacheService cacheService;

    @InjectMocks
    private CategoryKeywordServiceImpl service;

    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1).name("Technology").build();
    }

    @Test
    void getAll_ShouldReturnAllKeywordsAsDto() {
        CategoryKeyword keyword = CategoryKeyword.builder().id(10).keyword("AI").category(category).build();
        when(keywordRepo.findAll()).thenReturn(List.of(keyword));

        List<CategoryKeywordDto> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("AI", result.get(0).getKeyword());
        assertEquals("Technology", result.get(0).getCategoryName());
    }

    @Test
    void addKeyword_ShouldSaveKeywordAndRefreshCache() {
        CategoryKeywordDto dto = CategoryKeywordDto.builder()
                .keyword("Cloud")
                .categoryId(1)
                .build();

        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));

        CategoryKeyword keywordToSave = CategoryKeyword.builder()
                .keyword("cloud")
                .category(category)
                .build();

        when(keywordRepo.save(any(CategoryKeyword.class)))
                .thenAnswer(invocation -> {
                    CategoryKeyword arg = invocation.getArgument(0);
                    arg.setId(100); // simulate DB generated ID
                    return arg;
                });

        CategoryKeywordDto result = service.addKeyword(dto);

        assertEquals("cloud", result.getKeyword());
        assertEquals(1, result.getCategoryId());
        verify(cacheService).refreshCache();
    }

    @Test
    void addKeyword_ShouldThrowException_WhenCategoryNotFound() {
        when(categoryRepo.findById(anyInt())).thenReturn(Optional.empty());

        CategoryKeywordDto dto = CategoryKeywordDto.builder().keyword("AI").categoryId(99).build();

        assertThrows(ResourceNotFoundException.class, () -> service.addKeyword(dto));
    }

    @Test
    void deleteKeyword_ShouldInvokeRepositoryDelete() {
        service.deleteKeyword(5);
        verify(keywordRepo).deleteById(5);
    }

    @Test
    void getByCategoryId_ShouldReturnFilteredKeywords() {
        CategoryKeyword k1 = CategoryKeyword.builder().id(1).keyword("Java").category(category).build();
        CategoryKeyword k2 = CategoryKeyword.builder().id(2).keyword("Football")
                .category(Category.builder().id(2).name("Sports").build()).build();

        when(keywordRepo.findAll()).thenReturn(List.of(k1, k2));

        List<CategoryKeywordDto> result = service.getByCategoryId(1);

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getKeyword());
    }
}
