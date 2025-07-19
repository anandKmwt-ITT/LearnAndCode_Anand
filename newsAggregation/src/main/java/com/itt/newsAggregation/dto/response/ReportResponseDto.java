package com.itt.newsAggregation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponseDto {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private LocalDateTime reportedAt;
}
