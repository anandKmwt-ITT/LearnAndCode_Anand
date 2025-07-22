package com.itt.newsAggregation.dto.common;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private Integer id;
    private String title;
    private String content;
    private String source;
    private String url;
    private String category;
    private LocalDateTime publishedAt;
}