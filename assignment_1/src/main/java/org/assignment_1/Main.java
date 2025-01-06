package org.assignment_1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String jsonFilePath = "D:\\LearnAndCode_Anand\\assignment_1\\src\\main\\resources\\countries.json";

        JsonNode rootNode;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rootNode = objectMapper.readTree(new File(jsonFilePath));

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter a Country Code (e.g., IN/US/AUS) or 'exit' to quit:");

            while (true) {
                System.out.print("Country Code: ");
                String countryCode = scanner.nextLine().trim();

                if (countryCode.equalsIgnoreCase("EXIT")) {
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                }

                showCountryDetails(rootNode, countryCode.toUpperCase());
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private static void showCountryDetails(JsonNode rootNode, String countryCode) {
        JsonNode countryNode = rootNode.get(countryCode);
        if (countryNode != null) {
            String countryName = countryNode.get("name").asText();
            System.out.println("Country: " + countryName);

            JsonNode adjacentNode = countryNode.get("adjacent");
            if (adjacentNode != null && adjacentNode.isArray()) {
                List<String> adjacentCountries = new ArrayList<>();
                for (JsonNode adjacentCode : adjacentNode) {
                    JsonNode adjacentCountry = rootNode.get(adjacentCode.asText());
                    if (adjacentCountry != null) {
                        adjacentCountries.add(adjacentCountry.get("name").asText());
                    }
                }
                if (!adjacentCountries.isEmpty()) {
                    System.out.println("Adjacent Countries: " + String.join(", ", adjacentCountries));
                } else {
                    System.out.println("No adjacent countries are there.");
                }
            } else {
                System.out.println("No adjacent countries data found.");
            }
        } else {
            System.out.println("Country code not found.");
        }
    }
}