package com.itt.newsAggregationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryKeywordDto {
    public Integer id;
    public String keyword;
    public Integer categoryId;
    public String categoryName;
}