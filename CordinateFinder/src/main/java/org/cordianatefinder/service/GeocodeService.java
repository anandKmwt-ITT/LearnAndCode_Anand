package org.cordianatefinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cordianatefinder.adapter.GeocodeHttpClient;
import org.cordianatefinder.config.Constants;
import org.cordianatefinder.model.GeocodeResult;

import java.io.IOException;

public class GeocodeService {

    private final ObjectMapper mapper;
    private final GeocodeHttpClient httpClient;

    public GeocodeService(GeocodeHttpClient httpClient) {
        this.mapper = new ObjectMapper();
        this.httpClient = httpClient;
    }

    public void printCoordinates(String city) {
        try {
            String formattedCity = city.trim().replace(" ", "+");
            String url = Constants.GEOCODE_BASE_URL + formattedCity + Constants.API_KEY;

            String response = httpClient.get(url);
            GeocodeResult[] results = mapper.readValue(response, GeocodeResult[].class);

            if (results.length > 0) {
                System.out.println(Constants.LATITUDE_LABEL + results[0].getLatitude());
                System.out.println(Constants.LONGITUDE_LABEL + results[0].getLongitude());
            } else {
                System.out.println(Constants.NO_RESULT_FOUND + city);
            }
        } catch (IOException e) {
            System.out.println(Constants.ERROR_FETCHING_COORDINATES + e.getMessage());
        }
    }
}