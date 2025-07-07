package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SavedArticleDto {
    public int id;
    public String title;
    public String source;
    public String url;
    public String category;

    @Override
    public String toString() {
        return String.format(
                "🆔 ID       : %d%n" +
                        "📰 Title    : %s%n" +
                        "🏷️ Category : %s%n" +
                        "🌐 Source   : %s%n" +
                        "🔗 URL      : %s%n",
                id, title, category, source, url
        );
    }
}
