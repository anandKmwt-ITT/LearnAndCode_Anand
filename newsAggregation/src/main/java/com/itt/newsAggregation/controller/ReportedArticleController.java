package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.request.ReportRequestDto;
import com.itt.newsAggregation.dto.response.ReportResponseDto;
import com.itt.newsAggregation.service.ReportedArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reported-articles")
@RequiredArgsConstructor
public class ReportedArticleController {

    private final ReportedArticleService reportedArticleService;

    @PostMapping
    public ResponseEntity<ReportResponseDto> reportArticle(@RequestBody ReportRequestDto dto) {
        return ResponseEntity.ok(reportedArticleService.reportArticle(dto));
    }
}
