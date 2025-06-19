package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.ArticleDto;
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

    @PostMapping("/save/{userId}/{articleId}")
    public ResponseEntity<SavedArticleDto> saveArticle(@PathVariable Integer userId, @PathVariable Integer articleId){
        SavedArticleDto savedArticleDto = articleService.saveArticle(userId, articleId);
        return new ResponseEntity<>(savedArticleDto, HttpStatus.CREATED);
    }

    @GetMapping("/saved/{userId}")
    public ResponseEntity<List<SavedArticleDto>> getSavedArticles(@PathVariable Integer userId) {
        List<SavedArticleDto> savedArticles = articleService.getSavedArticles(userId);
        return new ResponseEntity<>(savedArticles, HttpStatus.OK);
    }
}
