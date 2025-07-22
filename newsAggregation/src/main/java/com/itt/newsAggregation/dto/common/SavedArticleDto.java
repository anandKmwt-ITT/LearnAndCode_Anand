package com.itt.newsAggregation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavedArticleDto {
    private Integer userId;
    private Integer articleId;
    private LocalDateTime savedAt;
}
