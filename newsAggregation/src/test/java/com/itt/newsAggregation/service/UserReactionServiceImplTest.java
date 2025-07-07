package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.UserReactionDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Article;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.model.UserReaction;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.UserReactionRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.UserReactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserReactionServiceImplTest {

    @InjectMocks
    private UserReactionServiceImpl service;

    @Mock
    private UserReactionRepository reactionRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ArticleRepository articleRepo;

    private User user;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1).username("john").build();
        article = Article.builder().id(100).title("Sample").build();
    }

    @Test
    void addOrUpdateReaction_shouldAddNewReaction() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.LIKE);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(articleRepo.findById(100)).thenReturn(Optional.of(article));
        when(reactionRepo.findByUserIdAndArticleId(1, 100)).thenReturn(Optional.empty());

        boolean result = service.addOrUpdateReaction(dto);

        assertTrue(result);
        verify(reactionRepo).save(any(UserReaction.class));
    }

    @Test
    void addOrUpdateReaction_shouldUpdateReactionIfDifferent() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.DISLIKE);
        UserReaction existing = UserReaction.builder()
                .user(user)
                .article(article)
                .reaction(UserReaction.ReactionType.LIKE)
                .build();

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(articleRepo.findById(100)).thenReturn(Optional.of(article));
        when(reactionRepo.findByUserIdAndArticleId(1, 100)).thenReturn(Optional.of(existing));

        boolean result = service.addOrUpdateReaction(dto);

        assertTrue(result);
        verify(reactionRepo).save(existing);
        assertEquals(UserReaction.ReactionType.DISLIKE, existing.getReaction());
    }

    @Test
    void addOrUpdateReaction_shouldDoNothingIfSameReactionExists() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.LIKE);
        UserReaction existing = UserReaction.builder()
                .user(user)
                .article(article)
                .reaction(UserReaction.ReactionType.LIKE)
                .build();

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(articleRepo.findById(100)).thenReturn(Optional.of(article));
        when(reactionRepo.findByUserIdAndArticleId(1, 100)).thenReturn(Optional.of(existing));

        boolean result = service.addOrUpdateReaction(dto);

        assertFalse(result);
        verify(reactionRepo, never()).save(any());
    }

    @Test
    void addOrUpdateReaction_shouldThrowUserNotFound() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.LIKE);

        when(userRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addOrUpdateReaction(dto));
    }

    @Test
    void addOrUpdateReaction_shouldThrowArticleNotFound() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.LIKE);

        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(articleRepo.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.addOrUpdateReaction(dto));
    }

    @Test
    void removeReaction_shouldCallRepositoryDelete() {
        UserReactionDto dto = new UserReactionDto(1, 100, UserReaction.ReactionType.LIKE);

        service.removeReaction(dto);

        verify(reactionRepo).deleteByUserIdAndArticleId(1, 100);
    }

    @Test
    void getReactionCountsForArticle_shouldReturnLikeAndDislikeCounts() {
        when(reactionRepo.countByArticleIdAndReaction(100, UserReaction.ReactionType.LIKE)).thenReturn(5L);
        when(reactionRepo.countByArticleIdAndReaction(100, UserReaction.ReactionType.DISLIKE)).thenReturn(2L);

        Map<String, Long> result = service.getReactionCountsForArticle(100);

        assertEquals(5L, result.get("likes"));
        assertEquals(2L, result.get("dislikes"));
    }

    @Test
    void getUserReactions_shouldReturnReactionDtos() {
        UserReaction reaction = UserReaction.builder()
                .user(user)
                .article(article)
                .reaction(UserReaction.ReactionType.LIKE)
                .build();

        when(reactionRepo.findByUserId(1)).thenReturn(List.of(reaction));

        List<UserReactionDto> result = service.getUserReactions(1);

        assertEquals(1, result.size());
        assertEquals(UserReaction.ReactionType.LIKE, result.get(0).getReaction());
    }
}
