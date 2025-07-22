package com.itt.newsAggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NewsHeadlineResponseDto {
    private Integer id;
    private String title;
}
