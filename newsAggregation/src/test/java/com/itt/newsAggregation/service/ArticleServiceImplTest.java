package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.dto.common.UserReactionDto;
import com.itt.newsAggregation.exception.ResourceNotFoundException;
import com.itt.newsAggregation.model.*;
import com.itt.newsAggregation.notification.NotificationDispatcher;
import com.itt.newsAggregation.repository.ArticleRepository;
import com.itt.newsAggregation.repository.SavedArticleRepository;
import com.itt.newsAggregation.repository.UserReadArticleRepository;
import com.itt.newsAggregation.repository.UserRepository;
import com.itt.newsAggregation.service.impl.ArticleServiceImpl;
import com.itt.newsAggregation.service.impl.CategoryKeywordCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ArticleServiceImplTest {

    @Mock private ArticleRepository articleRepo;
    @Mock private SavedArticleRepository savedArticleRepo;
    @Mock private UserRepository userRepo;
    @Mock private CategoryService categoryService;
    @Mock private NotificationDispatcher notificationDispatcher;
    @Mock private UserReactionService reactionService;
    @Mock private UserReadArticleService readService;
    @Mock private UserReadArticleRepository readRepo;
    @Mock private CategoryKeywordCacheService keywordCacheService;

    @InjectMocks private ArticleServiceImpl service;

    @BeforeEach void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveArticles_newAndExistingUrls_triggersSaveAndNotifyOnlyNew() {
        ArticleDto dto1 = new ArticleDto(); dto1.setUrl("url1"); dto1.setTitle("t1"); dto1.setContent("c1");
        ArticleDto dto2 = new ArticleDto(); dto2.setUrl("url2"); dto2.setTitle("t2"); dto2.setContent("c2");
        List<ArticleDto> dtos = List.of(dto1, dto2);

        when(articleRepo.existsByUrl("url1")).thenReturn(true);
        when(articleRepo.existsByUrl("url2")).thenReturn(false);

        when(keywordCacheService.getCategoryForText(anyString())).thenReturn("Sports");
        when(categoryService.existsByName("Sports")).thenReturn(true);
        Category cat = new Category(); cat.setName("Sports");
        when(categoryService.findByName("Sports")).thenReturn(Optional.of(cat));

        Article saved = Article.builder().id(10).url("url2").build();
        when(articleRepo.saveAll(anyList())).thenReturn(List.of(saved));

        service.saveArticles(dtos);

        verify(articleRepo).saveAll(argThat(iterable -> {
            if (iterable instanceof List<?>) {
                return ((List<?>) iterable).size() == 1;
            } else if (iterable instanceof Collection<?>) {
                return ((Collection<?>) iterable).size() == 1;
            }
            // fallback for safety
            int count = 0;
            for (Object o : iterable) count++;
            return count == 1;
        }));

        verify(notificationDispatcher).notifyUsers(anyList());
    }

    @Test
    void readArticle_existing_callsMarkAsReadAndReturnsDto() {
        Article article = new Article(); article.setId(5); article.setTitle("A"); article.setContent("C"); article.setSource("S");
        article.setUrl("u"); article.setPublishedAt(LocalDateTime.now());
        Category cat = new Category(); cat.setName("X"); article.setCategory(cat);

        when(articleRepo.findById(5)).thenReturn(Optional.of(article));

        ArticleDto dto = service.readArticle(1, 5);
        assertEquals("A", dto.getTitle());
        verify(readService).markAsRead(1, 5);
    }

    @Test
    void readArticle_notFound_throws() {
        when(articleRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.readArticle(1, 1));
    }

    @Test
    void getLikedArticles_filtersLikes() {
        UserReactionDto like = new UserReactionDto(1, 10, UserReaction.ReactionType.LIKE);
        UserReactionDto dislike = new UserReactionDto(1, 20, UserReaction.ReactionType.DISLIKE);
        when(reactionService.getUserReactions(1)).thenReturn(List.of(like, dislike));

        Article a = new Article(); a.setId(10);
        when(articleRepo.findById(10)).thenReturn(Optional.of(a));
        ArticleDto result = new ArticleDto(); result.setId(10);
        // We'll stub getArticleById to return our result via spy
        ArticleServiceImpl spy = Mockito.spy(service);
        doReturn(result).when(spy).getArticleById(10);

        List<ArticleDto> res = spy.getLikedArticles(1);
        assertEquals(1, res.size());
        assertEquals(10, res.get(0).getId());
    }

    @Test
    void getViewedArticles_returnsMapped() {
        Article a1 = new Article(); a1.setId(100);
        UserReadArticle ura = new UserReadArticle(); ura.setArticle(a1);
        when(readRepo.findByUserId(2)).thenReturn(List.of(ura));

        ArticleDto outDto = new ArticleDto(); outDto.setId(100);
        ArticleServiceImpl spy = Mockito.spy(service);
        doReturn(outDto).when(spy).getArticleById(100);

        List<ArticleDto> res = spy.getViewedArticles(2);
        assertEquals(1, res.size());
        assertEquals(100, res.get(0).getId());
    }

    @Test
    void getSavedArticles_returnsDtos() {
        SavedArticle sa = new SavedArticle();
        Article a2 = new Article(); a2.setId(200);
        sa.setArticle(a2);
        when(savedArticleRepo.findByUserUsername("u")).thenReturn(List.of(sa));
        ArticleDto dto = new ArticleDto(); dto.setId(200);
        ArticleServiceImpl spy = Mockito.spy(service);
        doReturn(dto).when(spy).getArticleById(200);

        List<ArticleDto> res = spy.getSavedArticles("u");
        assertEquals(1, res.size());
        assertEquals(200, res.get(0).getId());
    }
}
