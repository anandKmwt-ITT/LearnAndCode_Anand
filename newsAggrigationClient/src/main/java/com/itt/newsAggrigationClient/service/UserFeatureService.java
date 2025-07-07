package com.itt.newsAggrigationClient.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsAggrigationClient.models.*;
import com.itt.newsAggrigationClient.util.ApiEndpoints;
import com.itt.newsAggrigationClient.util.ConsoleUtils;

import java.net.URI;
import java.net.http.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.itt.newsAggrigationClient.util.ApiEndpoints.*;
import static com.itt.newsAggrigationClient.util.HttpStatusCodes.Created;
import static com.itt.newsAggrigationClient.util.HttpStatusCodes.OK;

public class UserFeatureService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private Integer categoryId = null;

    public void showHeadlines(String token, Integer userId) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n📅 Select headline filter:");
        System.out.println("1. Today");
        System.out.println("2. Date Range");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");
        String dateChoice = scanner.nextLine();

        String startDate = null;
        String endDate = null;

        switch (dateChoice) {
            case "1" -> {
                String today = java.time.LocalDate.now().toString();
                startDate = today;
                endDate = today;
            }
            case "2" -> {
                System.out.print("Enter start date (yyyy-MM-dd): ");
                startDate = scanner.nextLine();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                endDate = scanner.nextLine();
            }
            case "3" -> {
                System.out.println("🔙 Returning to main menu...");
                return;
            }
            default -> {
                System.out.println("❌ Invalid option.");
                return;
            }
        }

        List<CategoryDto> categories = fetchCategories(token);
        if (categories.isEmpty()) {
            System.out.println("❌ No categories available.");
            return;
        }

        System.out.println("\nPlease choose a category:");
        System.out.println("0. All");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i).name);
        }
        System.out.print("Enter your choice: ");
        String input = scanner.nextLine();

        try {
            int index = Integer.parseInt(input);
            if (index == 0) {
                categoryId = null;
            } else if (index >= 1 && index <= categories.size()) {
                categoryId = categories.get(index - 1).id;
            } else {
                System.out.println("❌ Invalid category number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
            return;
        }


        try {
            StringBuilder uriBuilder = new StringBuilder(API_ARTICLES_QUERY);
            if (categoryId != null) uriBuilder.append("categoryId=").append(categoryId).append("&");
            if (startDate != null) uriBuilder.append("startDate=").append(startDate).append("&");
            if (endDate != null) uriBuilder.append("endDate=").append(endDate);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uriBuilder.toString()))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<NewsHeadlineResponseDto> headlines = mapper.readValue(response.body(), new TypeReference<>() {});
                if (headlines.isEmpty()) {
                    System.out.println("No headlines found.");
                    return;
                }

                System.out.println("\n===================================== HEADLINES ====================================\n");
                for (int i = 0; i < headlines.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, headlines.get(i).title);
                }

                while (true) {
                    System.out.println("\n===========================================");
                    System.out.println("1. View full article by number");
                    System.out.println("2. Back");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1" -> {
                            System.out.print("Enter headline number: ");
                            try {
                                int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;
                                if (selectedIndex >= 0 && selectedIndex < headlines.size()) {
                                    int articleId = headlines.get(selectedIndex).id;
                                    showFullArticle(token, articleId, userId);
                                } else {
                                    System.out.println("❌ Invalid number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("❌ Invalid input.");
                            }
                        }
                        case "2" -> {
                            System.out.println("🔙 Returning...");
                            return;
                        }
                        default -> System.out.println("❌ Invalid option.");
                    }
                }

            } else {
                System.out.println("❌ Failed to fetch headlines. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showSavedArticles(String token, String username) {
        ConsoleUtils.printGreeting(username);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ARTICLES_SAVED + username))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<SavedArticleDto> articles = mapper.readValue(response.body(), new TypeReference<List<SavedArticleDto>>() {});
                if (articles.isEmpty()) {
                    System.out.println("No saved articles.\n");
                } else {
                    System.out.println("\n\n--- Saved Articles ---\n\n");
                    articles.forEach(System.out::println);
                }
            } else {
                System.out.println("Failed to fetch saved articles.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showLikedArticles(String token, Integer userId) {
        try {
            String url = ApiEndpoints.API_ARTICLES_LIKED + userId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<ArticleDto> likedArticles = mapper.readValue(response.body(), new TypeReference<>() {});
                if (likedArticles.isEmpty()) {
                    System.out.println("🫥 You haven't liked any articles yet.");
                } else {
                    System.out.println("\n❤️ Your Liked Articles:\n");
                    for (int i = 0; i < likedArticles.size(); i++) {
                        ArticleDto article = likedArticles.get(i);
                        System.out.printf("%d. 📰 %s (%s)\nURL: %s\n\n", i + 1, article.title, article.source, article.url);
                    }
                }
            } else {
                System.out.println("❌ Failed to fetch liked articles. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error fetching liked articles: " + e.getMessage());
        }
    }


    public void showNotifications(String token, String username) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_NOTIFICATIONS_USER + username))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());

            if (response.statusCode() == OK) {
                List<Notification> notifications = mapper.readValue(response.body(), new TypeReference<>() {
                });
                if (notifications.isEmpty()) {
                    System.out.println("No notifications.");
                } else {
                    System.out.println("--- Notifications ---");
                    notifications.forEach(System.out::println);
                }
            } else {
                System.out.println("Failed to fetch notifications.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void searchArticles(String token) {
        System.out.print("Enter keyword to search: ");
        Scanner scanner = new Scanner(System.in);
        String keyword = scanner.nextLine();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ARTICLES_SEARCH_QUERY + keyword))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<ArticleDto> articles = mapper.readValue(response.body(), new TypeReference<List<ArticleDto>>() {});
                if (articles.isEmpty()) {
                    System.out.println("🔍 No articles found for keyword: " + keyword);
                } else {
                    System.out.println("\n\n--- Search Results ---\n\n");
                    for (ArticleDto article : articles) {
                        System.out.printf("📰 %s (%s)\nURL: %s\n\n", article.title, article.source, article.url);
                    }
                }
            } else {
                System.out.println("❌ Failed to fetch search results. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error during search: " + e.getMessage());
        }
    }

    public void notificationMenu(String token, String username, Integer userId) {
        ConsoleUtils.printGreeting(username);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nN O T I F I C A T I O N S");
            System.out.println("=====================================");
            System.out.println("1. View Notifications");
            System.out.println("2. Configure Notifications");
            System.out.println("3. Back");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> showNotifications(token, username);
                case "2" -> configureNotifications(token, username, userId);
                case "3" -> {
                    System.out.println("🔙 Returning to main menu...");
                    return;
                }
                case "4" -> {
                    System.out.println("👋 Logging out...");
                    System.exit(0);
                }
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        }
    }

    private void configureNotifications(String token, String username, Integer userId) {
        Scanner scanner = new Scanner(System.in);

        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_NOTIFICATION_PREFERENCES + username))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            if (getResponse.statusCode() != OK) {
                System.out.println("❌ Failed to fetch preferences.");
                return;
            }

            List<NotificationPreferenceDto> preferences = mapper.readValue(
                    getResponse.body(),
                    new TypeReference<>() {}
            );

            while (true) {
                System.out.println("\n--- CONFIGURE NOTIFICATIONS ---");
                for (int i = 0; i < preferences.size(); i++) {
                    NotificationPreferenceDto pref = preferences.get(i);
                    String categoryName = pref.category;
                    Integer categoryId = getCategoryIdByName(token, categoryName);

                    String keywordsDisplay = "";
                    if (categoryId != null) {
                        List<String> keywords = getKeywordsForCategoryAndUser(token, userId, categoryId);
                        if (!keywords.isEmpty()) {
                            keywordsDisplay = " | Keywords: " + String.join(", ", keywords);
                        }
                    }

                    System.out.printf("%d. %s - %s%s%n", i + 1, categoryName, pref.isEnabled ? "Enabled" : "Disabled", keywordsDisplay);
                }

                System.out.println((preferences.size() + 1) + ". ➕ Add Keyword");
                System.out.println((preferences.size() + 2) + ". 🔙 Back");
                System.out.println((preferences.size() + 3) + ". 🚪 Logout");

                System.out.print("Enter your option: ");
                String choice = scanner.nextLine();

                try {
                    int option = Integer.parseInt(choice);

                    if (option == preferences.size() + 2) return;
                    if (option == preferences.size() + 3) System.exit(0);

                    if (option == preferences.size() + 1) {
                        // ➕ Add Keyword
                        System.out.println("\nSelect a category to add keyword:");
                        for (int i = 0; i < preferences.size(); i++) {
                            NotificationPreferenceDto pref = preferences.get(i);
                            System.out.printf("%d. %s%n", i + 1, pref.category);
                        }

                        System.out.print("Enter category number: ");
                        String catInput = scanner.nextLine();
                        int catIndex;
                        try {
                            catIndex = Integer.parseInt(catInput) - 1;
                            if (catIndex < 0 || catIndex >= preferences.size()) {
                                System.out.println("❌ Invalid category choice.");
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("❌ Invalid number.");
                            continue;
                        }

                        NotificationPreferenceDto selectedPref = preferences.get(catIndex);
                        String categoryName = selectedPref.category;

                        Integer categoryId = getCategoryIdByName(token, categoryName);
                        if (categoryId == null) {
                            System.out.println("❌ Could not retrieve category ID for: " + categoryName);
                            continue;
                        }

                        System.out.print("Enter a keyword for category '" + categoryName + "': ");
                        String keyword = scanner.nextLine().trim();

                        if (keyword.isEmpty()) {
                            System.out.println("❌ Keyword cannot be empty.");
                            continue;
                        }

                        addKeywordToCategory(token, userId, categoryId, keyword);
                        continue;
                    }

                    if (option < 1 || option > preferences.size()) {
                        System.out.println("❌ Invalid choice.");
                        continue;
                    }

                    NotificationPreferenceDto selectedPref = preferences.get(option - 1);
                    System.out.printf("Toggle %s? (currently %s)%n", selectedPref.category, selectedPref.isEnabled ? "Enabled" : "Disabled");
                    System.out.print("Enter Y to toggle: ");
                    String toggle = scanner.nextLine();

                    if (toggle.equalsIgnoreCase("y")) {
                        selectedPref.isEnabled = !selectedPref.isEnabled;

                        HttpRequest updateRequest = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/api/notification-preferences"))
                                .header("Authorization", "Bearer " + token)
                                .header("Content-Type", "application/json")
                                .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(selectedPref)))
                                .build();

                        HttpResponse<String> updateResponse = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());
                        if (updateResponse.statusCode() == OK) {
                            System.out.println("✅ Updated preference.");
                        } else {
                            System.out.println("❌ Failed to update preference.");
                        }
                    }

                } catch (NumberFormatException e) {
                    System.out.println("❌ Invalid input.");
                }

            }

        } catch (Exception e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    private List<String> getKeywordsForCategoryAndUser(String token, int userId, int categoryId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_KEYWORDS_USER + userId + "/category/" + categoryId))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == OK) {
                List<KeywordDto> keywordDtos = mapper.readValue(response.body(), new TypeReference<>() {});
                return keywordDtos.stream().map(k -> k.name).toList();
            } else {
                System.out.println("⚠️ Failed to fetch keywords for categoryId=" + categoryId);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error fetching keywords: " + e.getMessage());
        }
        return List.of();
    }


    private Integer getCategoryIdByName(String token, String categoryName) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_CATEGORIES_NAME + categoryName))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == OK) {
                CategoryDto category = mapper.readValue(response.body(), CategoryDto.class);
                return category.id;
            } else {
                System.out.println("❌ Category not found. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error fetching category ID: " + e.getMessage());
        }
        return null;
    }

    private List<CategoryDto> fetchCategories(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_CATEGORIES))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                return mapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                System.out.println("❌ Failed to fetch categories. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error fetching categories: " + e.getMessage());
        }
        return List.of();
    }


    private void addKeywordToCategory(String token, int userId, int categoryId, String keyword) {
        try {
            Map<String, Object> requestBody = Map.of(
                    "name", keyword,
                    "userId", userId,
                    "categoryId", categoryId

            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_KEYWORDS))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == Created || response.statusCode() == OK) {
                System.out.println("✅ Keyword added successfully.");
            } else {
                System.out.println("❌ Failed to add keyword. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error adding keyword: " + e.getMessage());
        }
    }

    private void updateKeywordPreference(String token, String username, String keywords) {
        try {
            Map<String, Object> keywordPref = Map.of(
                    "username", username,
                    "categoryName", "Keywords",
                    "keywords", keywords,
                    "enabled", true
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/notification-preferences"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(keywordPref)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == OK) {
                System.out.println("✅ Keywords updated.");
            } else {
                System.out.println("❌ Failed to update keywords.");
            }

        } catch (Exception e) {
            System.out.println("Error updating keywords: " + e.getMessage());
        }
    }

    public void showViewedArticles(String token, Integer userId) {
        try {
            String url = API_ARTICLES_VIEWED + userId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<ArticleDto> viewedArticles = mapper.readValue(response.body(), new TypeReference<>() {});
                if (viewedArticles.isEmpty()) {
                    System.out.println("🫥 You haven't read any articles yet.");
                } else {
                    System.out.println("\n📖 Articles You've Read:\n");
                    for (int i = 0; i < viewedArticles.size(); i++) {
                        ArticleDto article = viewedArticles.get(i);
                        System.out.printf("%d. 📰 %s (%s)\nURL: %s\n\n", i + 1, article.title, article.source, article.url);
                    }
                }
            } else {
                System.out.println("❌ Failed to fetch viewed articles. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("⚠️ Error fetching viewed articles: " + e.getMessage());
        }
    }


    private void showFullArticle(String token, int articleId, int userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ARTICLES_READ + userId + "/"+ articleId))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                ArticleDto article = mapper.readValue(response.body(), ArticleDto.class);

                System.out.println("\n=========== Full Article ===========");
                System.out.println("📰 Title      : " + article.title);
                System.out.println("🏷️ Category   : " + article.category);
                System.out.println("🔗 Source     : " + article.source);
                System.out.println("🌐 URL        : " + article.url);
                System.out.println("📝 Content    :\n" + article.content);
                System.out.println("====================================\n");

                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("1. Save Article");
                    System.out.println("2. React to Article");
                    System.out.println("3. Back");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1" -> saveArticle(token, userId, articleId); // assumes userId is passed or stored
                        case "2" -> reactToArticle(token, userId, articleId);
                        case "3" -> {
                            return;
                        }
                        default -> System.out.println("❌ Invalid choice.");
                    }
                }

            } else {
                System.out.println("❌ Article not found. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error fetching article: " + e.getMessage());
        }
    }

    private void reactToArticle(String token, int userId, int articleId) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("React to Article:");
            System.out.println("1. 👍 Like");
            System.out.println("2. 👎 Dislike");
            System.out.println("3. ❌ Remove Reaction");
            System.out.println("4. 🔙 Back");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            String reaction = null;

            switch (choice) {
                case "1":
                    reaction = "LIKE";
                    break;
                case "2":
                    reaction = "DISLIKE";
                    break;
                case "3":
                    reaction = "UNLIKE";
                    break;
                case "4":
                    return;
                default:
                    System.out.println("❌ Invalid option.");
                    continue;
            }

            try {
                String url = API_REACTIONS;
                HttpRequest request;

                String jsonBody = mapper.writeValueAsString(Map.of(
                        "userId", userId,
                        "articleId", articleId,
                        "reaction", reaction
                ));

                if ("UNLIKE".equals(reaction)) {
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .method("DELETE", HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                    System.out.println(jsonBody);
                } else {
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(url))
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                            .build();
                }

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == OK) {
                    System.out.println("✅ Reaction processed: " + reaction);
                }
                else {
                    System.out.println("❌ Failed to process reaction. Status: " + response.statusCode());
                }

            } catch (Exception e) {
                System.out.println("Error sending reaction: " + e.getMessage());
            }
        }
    }

    private void saveArticle(String token, int userId, int articleId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ARTICLES_SAVE + userId + "/" + articleId))
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == Created) {
                System.out.println("✅ Article saved successfully!");
            } else {
                System.out.println("❌ Failed to save article. Status: " + response.statusCode());
            }

        } catch (Exception e) {
            System.out.println("Error saving article: " + e.getMessage());
        }
    }
}