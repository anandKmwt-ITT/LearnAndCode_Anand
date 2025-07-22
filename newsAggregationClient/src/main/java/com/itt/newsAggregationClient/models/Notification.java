package com.itt.newsAggregationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    public String message;
    public String sentAt;

    @Override
    public String toString() {
        return "[" + sentAt + "] " + message + "\n\n";
    }
}
