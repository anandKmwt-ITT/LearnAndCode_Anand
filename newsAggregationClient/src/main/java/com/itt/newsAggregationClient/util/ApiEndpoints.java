package com.itt.newsAggregationClient.util;

public class ApiEndpoints {

    //User
    public static final String API_ARTICLES_LIKED = "http://localhost:8080/api/articles/liked/";
    public static final String API_ARTICLES_READ = "http://localhost:8080/api/articles/read/";
    public static final String API_ARTICLES_SAVE = "http://localhost:8080/api/articles/save/";
    public static final String API_ARTICLES_SAVED = "http://localhost:8080/api/articles/saved/";
    public static final String API_ARTICLES_SEARCH_QUERY = "http://localhost:8080/api/articles/search?keyword=";
    public static final String API_ARTICLES_VIEWED = "http://localhost:8080/api/articles/viewed/";
    public static final String API_ARTICLES_QUERY = "http://localhost:8080/api/articles?";
    public static final String API_CATEGORIES = "http://localhost:8080/api/categories";
    public static final String API_CATEGORIES_NAME = "http://localhost:8080/api/categories/name/";
    public static final String API_KEYWORDS = "http://localhost:8080/api/keywords";
    public static final String API_KEYWORDS_USER = "http://localhost:8080/api/keywords/user/";
    public static final String API_NOTIFICATION_PREFERENCES = "http://localhost:8080/api/notification-preferences/";
    public static final String API_NOTIFICATIONS_USER = "http://localhost:8080/api/notifications/user/";
    public static final String API_REACTIONS = "http://localhost:8080/api/reactions";

    //Admin
    public static final String API_ADMIN_CLIENTS = "http://localhost:8080/api/clients";
    public static final String API_ADMIN_CATEGORIES = "http://localhost:8080/api/categories";
    public static final String API_ADMIN_CATEGORY_KEYWORDS = "http://localhost:8080/api/category-keywords";
}
