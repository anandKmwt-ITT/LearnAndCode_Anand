package com.itt.newsAggregation.dto.common;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryKeywordDto {
    private Integer id;
    private String keyword;
    private Integer categoryId;
    private String categoryName;
}