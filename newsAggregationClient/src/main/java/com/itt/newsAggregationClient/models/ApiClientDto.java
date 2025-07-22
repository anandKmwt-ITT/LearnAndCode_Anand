package com.itt.newsAggregationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // ✅ This is the fix
public class ApiClientDto {
    public Integer id;
    public String name;
    public String apiKey;
    public String status;
    public String lastAccessed;

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Status: %s | Last Accessed: %s",
                id, name, status, lastAccessed);
    }
}