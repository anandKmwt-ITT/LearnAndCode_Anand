package com.itt.newsAggregation.service;

import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.UserReadArticleRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.UserReadArticleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserReadArticleServiceImplTest {

    @InjectMocks
    private UserReadArticleServiceImpl service;

    @Mock
    private UserReadArticleRepository readRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ArticleRepository articleRepo;

    private final int userId = 1;
    private final int articleId = 100;

    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder().id(userId).username("john").build();
        article = Article.builder().id(articleId).title("Test Article").build();
    }

    @Test
    void markAsRead_shouldSaveEntryIfNotExists() {
        when(readRepo.existsByUserIdAndArticleId(userId, articleId)).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(articleRepo.findById(articleId)).thenReturn(Optional.of(article));

        service.markAsRead(userId, articleId);

        verify(readRepo).save(argThat(readEntry ->
                readEntry.getUser().getId().equals(userId) &&
                        readEntry.getArticle().getId().equals(articleId)
        ));
    }

    @Test
    void markAsRead_shouldDoNothingIfAlreadyExists() {
        when(readRepo.existsByUserIdAndArticleId(userId, articleId)).thenReturn(true);

        service.markAsRead(userId, articleId);

        verify(readRepo, never()).save(any());
        verifyNoInteractions(userRepo);
        verifyNoInteractions(articleRepo);
    }

    @Test
    void markAsRead_shouldThrowUserNotFound() {
        when(readRepo.existsByUserIdAndArticleId(userId, articleId)).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.markAsRead(userId, articleId));
        verify(readRepo, never()).save(any());
    }

    @Test
    void markAsRead_shouldThrowArticleNotFound() {
        when(readRepo.existsByUserIdAndArticleId(userId, articleId)).thenReturn(false);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(articleRepo.findById(articleId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.markAsRead(userId, articleId));
        verify(readRepo, never()).save(any());
    }
}
