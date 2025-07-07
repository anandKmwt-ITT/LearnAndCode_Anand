package com.itt.newsAggrigationClient.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itt.newsAggrigationClient.Main;
import com.itt.newsAggrigationClient.models.AuthRequest;
import com.itt.newsAggrigationClient.models.AuthResponse;
import com.itt.newsAggrigationClient.models.User;

import java.net.URI;
import java.net.http.*;
import java.util.Scanner;

public class AuthService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public void registerUser(Scanner scanner) {
        System.out.println("=== User Registration ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User(username, email, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/user/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(user)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 201){
                System.out.println("Registration successful!!!\n");
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public AuthResponse loginUser(Scanner scanner) {
        System.out.println("=== User Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        AuthRequest authRequest = new AuthRequest(username, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/user/authenticate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(authRequest)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
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

}