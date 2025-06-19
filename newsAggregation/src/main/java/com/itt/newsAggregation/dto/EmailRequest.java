package com.itt.newsAggregation.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
}
