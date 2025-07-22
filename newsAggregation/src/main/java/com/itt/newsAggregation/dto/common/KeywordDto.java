package com.itt.newsAggregation.dto.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeywordDto {
    private Integer id;
    private String name;
    private Integer userId;
    private Integer categoryId;
}
