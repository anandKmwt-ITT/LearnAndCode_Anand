package com.itt.newsAggregationClient;

import com.itt.newsAggregationClient.models.AuthResponse;
import com.itt.newsAggregationClient.service.AuthService;
import com.itt.newsAggregationClient.service.UserFeatureService;
import com.itt.newsAggregationClient.service.AdminFeatureService;
import com.itt.newsAggregationClient.util.ConsoleUtils;

import java.util.Scanner;

public class Main {

    private final AdminFeatureService adminService = new AdminFeatureService();
    private final UserFeatureService userFeatureService = new UserFeatureService();
    private final AuthService userService = new AuthService();
    private final Scanner scanner = new Scanner(System.in);
    public static String username = null;
    private int userId = 0;

    public static void main(String[] args) {
        new Main().run(); // run the instance method
    }

    private void run() {
        String token = null;

        while (true) {
            System.out.println("\n===== News Aggregation Client =====");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            if (token != null) {
                System.out.println("Logged in as: " + token.substring(0, Math.min(15, token.length())) + "...");
            }

            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> userService.registerUser(scanner);
                case "2" -> {
                    AuthResponse auth = userService.loginUser(scanner);
                    if (auth != null && auth.token != null) {
                        this.userId = auth.currentUserId;
                        if ("ROLE_ADMIN".equalsIgnoreCase(auth.role)) {
                            showAdminMenu(auth.token);
                        } else {
                            showUserMenu(auth.token);
                        }
                    }
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showAdminMenu(String token) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View external servers and status");
            System.out.println("2. View external server details");
            System.out.println("3. Update/Edit external server");
            System.out.println("4. Add new news category");
            System.out.println("5. Add keyword to category");
            System.out.println("6. Toggle Category visibility");
            System.out.println("7. Toggle Article visibility based on KEYWORD");
            System.out.println("8. Toggle Article visibility");
            System.out.println("9. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> adminService.viewAllClients(token);
                case "2" -> adminService.viewClientDetails(scanner, token);
                case "3" -> adminService.updateClient(scanner, token);
                case "4" -> adminService.registerNewCategory(scanner, token);
                case "5" -> adminService.addKeywordToCategory(scanner, token);
                case "6" -> adminService.toggleCategoryVisibility(scanner, token);
                case "7" -> adminService.toggleArticleVisibilityByKeyword(scanner, token);
                case "8" -> adminService.toggleArticleVisibilityById(scanner, token);
                case "9" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showUserMenu(String token) {
        while (true) {
            ConsoleUtils.printGreeting(username);
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Headlines");
            System.out.println("2. Saved Articles");
            System.out.println("3. Liked Articles");
            System.out.println("4. Viewed Articles");
            System.out.println("5. Search");
            System.out.println("6. Notifications");
            System.out.println("7. Logout");
            System.out.print("Select option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> userFeatureService.showHeadlines(token, userId);
                case "2" -> userFeatureService.showSavedArticles(token, username);
                case "3" -> userFeatureService.showLikedArticles(token, userId);
                case "4" -> userFeatureService.showViewedArticles(token, userId);
                case "5" -> userFeatureService.searchArticles(token);
                case "6" -> userFeatureService.notificationMenu(token, username, userId);
                case "7" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}