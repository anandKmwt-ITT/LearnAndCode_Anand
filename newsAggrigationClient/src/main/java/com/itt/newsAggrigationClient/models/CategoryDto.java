package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    public Integer id;
    public String name;

    public CategoryDto() {}

    public CategoryDto(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{name='" + name + "'}";
    }
}
