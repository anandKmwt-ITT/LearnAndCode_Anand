package com.itt.newsAggregation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ApiClientResponseDto {
    private Integer id;
    private String name;
    private String url;
    private String apiKey;
    private String status;
    private String lastAccessed;
}