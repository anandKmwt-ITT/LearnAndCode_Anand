package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationPreferenceDto {
    public String username;
    public String category;
    public boolean isEnabled;
}
