package com.itt.newsAggregationClient.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsAggregationClient.Main;
import com.itt.newsAggregationClient.models.AuthRequest;
import com.itt.newsAggregationClient.models.AuthResponse;
import com.itt.newsAggregationClient.models.User;

import java.io.Console;
import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

import static com.itt.newsAggregationClient.util.HttpStatusCodes.Created;
import static com.itt.newsAggregationClient.util.HttpStatusCodes.OK;

public class AuthService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void registerUser(Scanner scanner) {
        System.out.println("=== User Registration ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String password = readPassword(scanner, "Password: ");

        User user = new User(username, email, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/user/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(user)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == Created) {
                System.out.println("Registration successful!!!\n");
            } else {
                System.out.println("Registration failed. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse loginUser(Scanner scanner) {
        System.out.println("=== User Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        String password = readPassword(scanner, "Password: ");

        AuthRequest authRequest = new AuthRequest(username, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/user/authenticate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(authRequest)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == OK) {
                AuthResponse authResponse = mapper.readValue(response.body(), AuthResponse.class);
                Main.username = username;
                System.out.println("Login successful.");
                return authResponse;
            } else {
                System.out.println("Login failed. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return null;
    }

    private String readPassword(Scanner scanner, String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword(prompt);
            return new String(passwordChars);
        } else {
            System.out.print(prompt + " (input will be visible): ");
            return scanner.nextLine();
        }
    }


}