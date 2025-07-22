package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.request.ReportRequestDto;
import com.itt.newsAggregation.dto.response.ReportResponseDto;

public interface ReportedArticleService {
    ReportResponseDto reportArticle(ReportRequestDto requestDto);
}
