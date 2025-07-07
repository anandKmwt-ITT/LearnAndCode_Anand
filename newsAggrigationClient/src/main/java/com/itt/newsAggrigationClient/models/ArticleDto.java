package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDto {
    public Integer id;
    public String title;
    public String content;
    public String source;
    public String url;
    public String category;

    @Override
    public String toString() {
        return """
        \n
        %s

        %s

        source: %s
        URL   : %s
        Category: %s
        """.formatted(id, title, content, source, url, category);
    }
}
