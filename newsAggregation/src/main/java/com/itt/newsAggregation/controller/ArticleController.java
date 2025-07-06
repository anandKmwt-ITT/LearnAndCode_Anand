package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.common.ArticleDto;
import com.itt.newsAggregation.dto.response.NewsHeadlineResponseDto;
import com.itt.newsAggregation.dto.common.SavedArticleDto;
import com.itt.newsAggregation.service.ArticleService;
import com.itt.newsAggregation.service.UserReadArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserReadArticleService userReadArticleService;

    @GetMapping
    public ResponseEntity<List<NewsHeadlineResponseDto>> getHeadlines
            (@RequestParam(required = false) Integer categoryId,
             @RequestParam(required = false) String startDate,
             @RequestParam(required = false) String endDate) {
        List<NewsHeadlineResponseDto> articles = articleService.getHeadlines(categoryId, startDate, endDate);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("/read/{userId}/{articleId}")
    public ResponseEntity<ArticleDto> readArticle(@PathVariable Integer userId, @PathVariable Integer articleId) {
        ArticleDto article = articleService.readArticle(userId, articleId);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable Integer id) {
        ArticleDto article = articleService.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("/headlines")
    public ResponseEntity<List<NewsHeadlineResponseDto>> getAllHeadlines() {
        List<NewsHeadlineResponseDto> headlines = articleService.getAllHeadlines();
        return new ResponseEntity<>(headlines, HttpStatus.OK);
    }

    @PostMapping("/save/{userId}/{articleId}")
    public ResponseEntity<SavedArticleDto> saveArticle(@PathVariable Integer userId, @PathVariable Integer articleId){
        SavedArticleDto savedArticleDto = articleService.saveArticle(userId, articleId);
        return new ResponseEntity<>(savedArticleDto, HttpStatus.CREATED);
    }

    @GetMapping("/saved/{username}")
    public ResponseEntity<List<ArticleDto>> getSavedArticles(@PathVariable String username) {
        List<ArticleDto> savedArticles = articleService.getSavedArticles(username);
        return new ResponseEntity<>(savedArticles, HttpStatus.OK);
    }

    @GetMapping("/liked/{userId}")
    public ResponseEntity<List<ArticleDto>> getLikedArticles(@PathVariable Integer userId) {
        List<ArticleDto> likedArticles = articleService.getLikedArticles(userId);
        return new ResponseEntity<>(likedArticles, HttpStatus.OK);
    }

    @GetMapping("/viewed/{userId}")
    public ResponseEntity<List<ArticleDto>> getViewedArticles(@PathVariable Integer userId) {
        List<ArticleDto> viewedArticles = articleService.getViewedArticles(userId);
        return new ResponseEntity<>(viewedArticles, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDto>> searchArticles(@RequestParam("keyword") String keyword) {
        List<ArticleDto> result = articleService.searchArticles(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ArticleDto>> getArticlesByDateRange(
            @RequestParam(required = false) String startDate, // format: yyyy-MM-dd
            @RequestParam(required = false) String endDate
    ) {
        List<ArticleDto> articles = articleService.getArticlesByDateRange(startDate, endDate);
        return ResponseEntity.ok(articles);
    }

}