package com.itt.newsAggrigationClient.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    public String token;
    public String role;
    public Integer currentUserId;

    @Override
    public String toString() {
        return "AuthResponse{token='" + token + "', role='" + role + "'}";
    }
}
