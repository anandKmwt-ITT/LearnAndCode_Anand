package com.itt.newsAggregation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationPreferenceDto {
    private String username;
    private String category;
    private Boolean isEnabled;
}
