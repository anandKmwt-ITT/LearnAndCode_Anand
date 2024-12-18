package org.assignment_1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
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

                JsonNode countryNode = rootNode.get(countryCode.toUpperCase());
                if (countryNode != null) {
                    String countryName = countryNode.get("name").asText();
                    System.out.println("Country: " + countryName);
                } else {
                    System.out.println("Country code not found.");
                }
            }

            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }
    }
}