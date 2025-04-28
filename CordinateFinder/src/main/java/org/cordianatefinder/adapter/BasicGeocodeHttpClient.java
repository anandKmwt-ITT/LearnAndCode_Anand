package org.cordianatefinder.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicGeocodeHttpClient	 implements GeocodeHttpClient {

    @Override
    public String get(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        StringBuilder responseBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }

        return responseBuilder.toString();
    }
}
