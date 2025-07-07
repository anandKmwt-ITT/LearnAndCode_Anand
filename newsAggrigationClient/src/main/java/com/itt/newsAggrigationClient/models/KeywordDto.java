package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeywordDto {
    public Integer id;
    public String name;
    public Integer userId;
    public Integer categoryId;
}
