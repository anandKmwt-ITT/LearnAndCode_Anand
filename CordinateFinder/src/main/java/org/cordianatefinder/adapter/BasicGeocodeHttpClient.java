package org.cordianatefinder.adapter;

import org.cordianatefinder.config.Constants;
import org.cordianatefinder.util.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BasicGeocodeHttpClient	implements GeocodeHttpClient {

    @Override
    public String get(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(HttpMethod.GET.name());
        connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
        connection.setReadTimeout(Constants.READ_TIMEOUT);

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
