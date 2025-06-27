package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.ArticleDto;
import com.itt.newsAggregation.dto.NewsHeadlineResponseDto;
import com.itt.newsAggregation.dto.SavedArticleDto;
import com.itt.newsAggregation.service.ArticleService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<ArticleDto> articles = articleService.getAllArticles();
        return new ResponseEntity<>(articles, HttpStatus.OK);
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
    public ResponseEntity<List<SavedArticleDto>> getSavedArticles(@PathVariable String username) {
        List<SavedArticleDto> savedArticles = articleService.getSavedArticles(username);
        return new ResponseEntity<>(savedArticles, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleDto>> searchArticles(@RequestParam("keyword") String keyword) {
        List<ArticleDto> result = articleService.searchArticles(keyword);
        return ResponseEntity.ok(result);
    }

}
