package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.request.CategoryRequestDto;
import com.itt.newsAggregation.dto.response.CategoryResponseDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.service.impl.CategoryKeywordCacheService;
import com.itt.newsAggregation.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepo;

    @Mock
    private CategoryKeywordCacheService cacheService;

    @InjectMocks
    private CategoryServiceImpl service;

    private final Category category = Category.builder().id(1).name("Technology").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory_ShouldCreateAndReturnDto_WhenNotExists() {
        CategoryRequestDto dto = CategoryRequestDto.builder().name("Technology").build();

        when(categoryRepo.existsByName("Technology")).thenReturn(false);
        when(categoryRepo.save(any(Category.class))).thenReturn(category);

        CategoryResponseDto result = service.createCategory(dto);

        assertNotNull(result);
        assertEquals("Technology", result.getName());
        verify(cacheService).refreshCache();
    }

    @Test
    void createCategory_ShouldThrowException_WhenCategoryAlreadyExists() {
        CategoryRequestDto dto = CategoryRequestDto.builder().name("Technology").build();
        when(categoryRepo.existsByName("Technology")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.createCategory(dto);
        });

        assertEquals("Category with name Technology already exists.", ex.getMessage());
        verify(categoryRepo, never()).save(any());
    }

    @Test
    void getCategoryById_ShouldReturnCategory_WhenExists() {
        when(categoryRepo.findById(1)).thenReturn(Optional.of(category));
        CategoryResponseDto result = service.getCategoryById(1);
        assertEquals("Technology", result.getName());
    }

    @Test
    void getCategoryById_ShouldThrow_WhenNotFound() {
        when(categoryRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getCategoryById(1));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        List<CategoryResponseDto> result = service.getAllCategories();
        assertEquals(1, result.size());
        assertEquals("Technology", result.get(0).getName());
    }

    @Test
    void findByName_ShouldReturnOptionalCategory() {
        when(categoryRepo.findByName("Technology")).thenReturn(Optional.of(category));
        Optional<Category> result = service.findByName("Technology");
        assertTrue(result.isPresent());
        assertEquals("Technology", result.get().getName());
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenExists() {
        when(categoryRepo.existsByName("Tech")).thenReturn(true);
        assertTrue(service.existsByName("Tech"));
    }

    @Test
    void getCategoryDetailsByName_ShouldReturnDto_WhenExists() {
        when(categoryRepo.findByName("Technology")).thenReturn(Optional.of(category));
        CategoryResponseDto result = service.getCategoryDetailsByName("Technology");
        assertEquals("Technology", result.getName());
    }

    @Test
    void getCategoryDetailsByName_ShouldThrow_WhenNotFound() {
        when(categoryRepo.findByName("Unknown")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getCategoryDetailsByName("Unknown"));
    }
}
