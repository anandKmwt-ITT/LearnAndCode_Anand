package org.assignment_3;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class TumblrClient {
    public static void main(String[] args) {
        try {
            processUserInput();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void processUserInput() throws IOException, JSONException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Tumblr blog name: ");
        String blogName = scanner.nextLine().trim();

        System.out.print("Enter post range (start-end): ");
        String rangeInput = scanner.nextLine().trim();

        String[] range = rangeInput.split("-");
        if (range.length != 2) {
            throw new IllegalArgumentException("Invalid input! Please enter the range in 'start-end' format.");
        }

        int startingBlog = Integer.parseInt(range[0].trim());
        int numberOfBlogs = Integer.parseInt(range[1].trim()) - startingBlog + 1;
        if (startingBlog < 1 || numberOfBlogs < 1) {
            throw new IllegalArgumentException("Invalid range values! Ensure 'start' is positive and 'end' is greater than 'start'.");
        }

        fetchAndDisplayTumblrData(blogName, startingBlog, numberOfBlogs);
    }

    private static void fetchAndDisplayTumblrData(String blogName, int start, int num) throws IOException, JSONException {
        String url = "https://" + blogName + ".tumblr.com/api/read/json?type=photo&num=" + num + "&start=" + (start - 1);
        String jsonResponse = fetchAPIResponse(url);
        displayBlogInfo(jsonResponse, start);
    }

    private static String fetchAPIResponse(String apiUrl) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(apiUrl).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = bufferedReader.readLine()) != null) {
            response.append(inputLine);
        }

        bufferedReader.close();
        return cleanJsonResponse(response.toString());
    }

    private static String cleanJsonResponse(String response) {
        if (response.startsWith("var tumblr_api_read =")) {
            return response.substring(21).trim();
        }
        return response;
    }

    private static void displayBlogInfo(String jsonResponse, int start) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject blog = jsonObject.getJSONObject("tumblelog");

        System.out.println("\nTitle: " + blog.optString("title", "N/A"));
        System.out.println("Name: " + blog.optString("name", "N/A"));
        System.out.println("Description: " + blog.optString("description", "N/A"));
        System.out.println("Total Posts: " + jsonObject.optInt("posts-total", 0));
        System.out.println("\nImages:");

        JSONArray posts = jsonObject.getJSONArray("posts");
        for (int count = 0; count < posts.length(); count++) {
            JSONObject post = posts.getJSONObject(count);
            System.out.println((start + count) + ". " + post.optString("photo-url-1280", "No Image Available"));
        }
    }
}
