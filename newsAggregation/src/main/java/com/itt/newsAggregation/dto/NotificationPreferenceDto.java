package com.itt.newsAggregation.dto;

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
