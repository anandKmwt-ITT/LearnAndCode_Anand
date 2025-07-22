package com.itt.newsAggregation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ApiClientDto {
    private String name;
    private String url;
    private String apiKey;
    private String status;
}