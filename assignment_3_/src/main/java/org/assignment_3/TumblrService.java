package org.assignment_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TumblrService {
    private static final String API_URL = "https://%s.tumblr.com/api/read/json?type=photo&num=%d&start=%d";

    public String fetchBlogData(String blogName, int start, int num) throws IOException {
        String url = String.format(API_URL, blogName, num, start - 1);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return cleanJsonResponse(response.toString());
    }

    private String cleanJsonResponse(String response) {
        int startIndex = response.indexOf('{');
        return (startIndex != -1) ? response.substring(startIndex) : response;
    }
}
