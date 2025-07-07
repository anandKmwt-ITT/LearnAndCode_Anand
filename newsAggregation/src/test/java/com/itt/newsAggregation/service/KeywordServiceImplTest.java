package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.KeywordDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Category;
import com.itt.newsAggregation.model.Keyword;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.CategoryRepository;
import com.itt.newsAggregation.repository.KeywordRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.KeywordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KeywordServiceImplTest {

    @Mock
    private KeywordRepository keywordRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private KeywordServiceImpl service;

    private final User user = User.builder().id(1).username("john").build();
    private final Category category = Category.builder().id(2).name("Tech").build();
    private final Keyword keyword = Keyword.builder()
            .id(10).name("AI").user(user).category(category).build();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createKeyword_shouldCreate_WhenValidInput() {
        KeywordDto dto = KeywordDto.builder()
                .name("AI")
                .userId(1)
                .categoryId(2)
                .build();

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(2)).thenReturn(Optional.of(category));
        when(keywordRepo.existsByNameAndUserIdAndCategoryId("AI", 1, 2)).thenReturn(false);
        when(keywordRepo.save(any())).thenReturn(keyword);

        KeywordDto result = service.createKeyword(dto);

        assertNotNull(result);
        assertEquals("AI", result.getName());
        assertEquals(1, result.getUserId());
        assertEquals(2, result.getCategoryId());
    }

    @Test
    void createKeyword_shouldThrow_WhenUserNotFound() {
        KeywordDto dto = KeywordDto.builder().name("AI").userId(99).categoryId(2).build();
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.createKeyword(dto));
    }

    @Test
    void createKeyword_shouldThrow_WhenCategoryNotFound() {
        KeywordDto dto = KeywordDto.builder().name("AI").userId(1).categoryId(99).build();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.createKeyword(dto));
    }

    @Test
    void createKeyword_shouldThrow_WhenKeywordExists() {
        KeywordDto dto = KeywordDto.builder().name("AI").userId(1).categoryId(2).build();

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(categoryRepo.findById(2)).thenReturn(Optional.of(category));
        when(keywordRepo.existsByNameAndUserIdAndCategoryId("AI", 1, 2)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> service.createKeyword(dto));
    }

    @Test
    void getKeywordsByUser_shouldReturnList() {
        when(keywordRepo.findByUserId(1)).thenReturn(List.of(keyword));

        List<KeywordDto> result = service.getKeywordsByUser(1);

        assertEquals(1, result.size());
        assertEquals("AI", result.get(0).getName());
    }

    @Test
    void getKeywordsByCategory_shouldReturnList() {
        when(keywordRepo.findByCategoryId(2)).thenReturn(List.of(keyword));

        List<KeywordDto> result = service.getKeywordsByCategory(2);

        assertEquals(1, result.size());
        assertEquals("AI", result.get(0).getName());
    }

    @Test
    void getKeywordsByUserAndCategory_shouldReturnList() {
        when(keywordRepo.findByUserIdAndCategoryId(1, 2)).thenReturn(List.of(keyword));

        List<KeywordDto> result = service.getKeywordsByUserAndCategory(1, 2);

        assertEquals(1, result.size());
        assertEquals("AI", result.get(0).getName());
    }
}
