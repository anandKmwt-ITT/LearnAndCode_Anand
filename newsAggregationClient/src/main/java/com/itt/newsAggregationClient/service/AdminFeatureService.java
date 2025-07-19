package com.itt.newsAggregationClient.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsAggregationClient.models.ApiClientDto;
import com.itt.newsAggregationClient.models.CategoryDto;
import com.itt.newsAggregationClient.models.CategoryKeywordDto;
import com.itt.newsAggregationClient.models.NewsHeadlineResponseDto;

import java.net.URI;
import java.net.http.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static com.itt.newsAggregationClient.util.ApiEndpoints.*;
import static com.itt.newsAggregationClient.util.HttpStatusCodes.Created;
import static com.itt.newsAggregationClient.util.HttpStatusCodes.OK;

public class AdminFeatureService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String BASE_URL = API_ADMIN_CLIENTS;


    public void viewAllClients(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<ApiClientDto> clients = mapper.readValue(response.body(), new TypeReference<>() {});
                System.out.println("--- External Servers and Status ---");
                for (ApiClientDto dto : clients) {
                    System.out.println(dto);
                }
            } else {
                System.out.println("Failed to fetch clients. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error fetching clients: " + e.getMessage());
        }
    }

    public void viewClientDetails(Scanner scanner, String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<ApiClientDto> clients = mapper.readValue(response.body(), new TypeReference<>() {});
                System.out.println("\nList of external server details:");
                int index = 1;
                for (ApiClientDto client : clients) {
                    System.out.printf("%d. %s - %s%n", index++, client.name, client.apiKey);
                }
            } else {
                System.out.println("Could not fetch client details. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error fetching client details: " + e.getMessage());
        }
    }


    public void updateClient(Scanner scanner, String token) {
        System.out.print("Enter client ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter updated API key: ");
        String newKey = scanner.nextLine();

        try {
            ApiClientDto updateDto = new ApiClientDto();
            updateDto.apiKey = newKey;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(updateDto)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                ApiClientDto updated = mapper.readValue(response.body(), ApiClientDto.class);
                System.out.println("Updated client: " + updated);
            } else {
                System.out.println("Update failed. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error updating client: " + e.getMessage());
        }
    }

    public void registerNewCategory(Scanner scanner, String token) {
        System.out.print("Enter new category name: ");
        String categoryName = scanner.nextLine();

        CategoryDto categoryDto = new CategoryDto(categoryName);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ADMIN_CATEGORIES))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(categoryDto)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == Created) {
                CategoryDto created = mapper.readValue(response.body(), CategoryDto.class);
                System.out.println("Category created: " + created.name);
            } else {
                System.out.println("Failed to create category. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error creating category: " + e.getMessage());
        }
    }

    public void addKeywordToCategory(Scanner scanner, String token) {
        viewAllCategories(token); // Show category list before adding

        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();

        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        CategoryKeywordDto keywordDto = new CategoryKeywordDto();
        keywordDto.keyword = keyword;
        keywordDto.categoryId = categoryId;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ADMIN_CATEGORY_KEYWORDS))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(keywordDto)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                CategoryKeywordDto created = mapper.readValue(response.body(), CategoryKeywordDto.class);
                System.out.println("✅ Keyword added: " + created.keyword + " to category: " + created.categoryName);
            } else {
                System.out.println("❌ Failed to add keyword. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("❗ Error adding keyword: " + e.getMessage());
        }
    }


    public void viewAllCategories(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_ADMIN_CATEGORIES))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                List<CategoryDto> categories = mapper.readValue(response.body(), new TypeReference<>() {});
                System.out.println("📋 Available Categories:");
                for (CategoryDto category : categories) {
                    System.out.printf("ID: %d | Name: %s | Hidden: %s%n", category.id, category.name, category.hidden);
                }
            } else {
                System.out.println("❌ Failed to fetch categories. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("❗ Error fetching categories: " + e.getMessage());
        }
    }

    public void toggleCategoryVisibility( Scanner scanner, String token) {
        try {
            viewAllCategories(token);

            System.out.print("Enter Category ID to toggle hidden status: ");
            int categoryId = Integer.parseInt(scanner.nextLine());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_HIDE_CATEGORY + categoryId))
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                System.out.println("✅ Category visibility toggled successfully.");
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("❌ Failed to toggle category visibility. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("❗ Error while toggling category visibility: " + e.getMessage());
        }
    }

    public void toggleArticleVisibilityByKeyword(Scanner scanner, String token) {
        try {

            System.out.print("Do you want to hide(true) or unhide(false) articles? (true/false): ");
            boolean hidden = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("Enter keyword to hide or unhide articles: ");
            String keyword = scanner.nextLine();

            String url = API_HIDE_ARTICLES_BY_KEYWORD + "?keyword=" + keyword
                    + "&hidden=" + hidden;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                System.out.println("✅ Articles visibility updated based on keyword.");
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("❌ Failed to update article visibility. Status: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            System.out.println("❗ Error while toggling article visibility: " + e.getMessage());
        }
    }

    public void toggleArticleVisibilityById(Scanner scanner, String token) {
        try {
            List<NewsHeadlineResponseDto> headlines = fetchAllHeadlines(token);
            System.out.println("\n=================Articles=================\n");
            if(!headlines.isEmpty()){
                for (int i = 0; i < headlines.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, headlines.get(i).title);
                }
            }

            System.out.print("\nEnter Article ID to update visibility: ");
            int articleId = Integer.parseInt(scanner.nextLine());

            System.out.print("Do you want to hide the article? (true/false): ");
            boolean hidden = Boolean.parseBoolean(scanner.nextLine());

            String url = API_HIDE_ARTICLE + articleId + "/visibility?hidden=" + hidden;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == OK) {
                System.out.println("✅ Article visibility updated successfully.");
                System.out.println("Response: " + response.body());
            } else {
                System.out.println("❌ Failed to update article visibility. Status: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            System.out.println("❗ Error while updating article visibility: " + e.getMessage());
        }
    }

    private List<NewsHeadlineResponseDto> fetchAllHeadlines(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_GET_HEADLINES))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return Arrays.asList(objectMapper.readValue(response.body(), NewsHeadlineResponseDto[].class));
            } else {
                System.out.println("⚠️ Failed to fetch headlines. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("❗ Error fetching headlines: " + e.getMessage());
        }
        return Collections.emptyList();
    }




}
