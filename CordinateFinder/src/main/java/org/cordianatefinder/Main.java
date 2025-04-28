package org.cordianatefinder;

import org.cordianatefinder.adapter.BasicGeocodeHttpClient;
import org.cordianatefinder.adapter.GeocodeHttpClient;
import org.cordianatefinder.config.Constants;
import org.cordianatefinder.service.GeocodeService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GeocodeHttpClient httpClient = new BasicGeocodeHttpClient();
        GeocodeService geocodeService = new GeocodeService(httpClient);

        System.out.print(Constants.PROMPT_ENTER_CITY);
        String city = scanner.nextLine();

        geocodeService.printCoordinates(city);
    }
}
