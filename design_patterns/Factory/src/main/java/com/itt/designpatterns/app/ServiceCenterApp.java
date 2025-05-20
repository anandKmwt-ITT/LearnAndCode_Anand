package com.itt.designpatterns.app;

import com.itt.designpatterns.service.VehicleService;
import com.itt.designpatterns.factory.VehicleServiceFactory;

public class ServiceCenterApp {
    public static void main(String[] args) {
        String[] vehicles = {"car", "bike", "truck"};

        for (String type : vehicles) {
            VehicleService service = VehicleServiceFactory.getVehicleService(type);
            System.out.println("Starting service for " + type.toUpperCase());
            service.performService();
            System.out.println();
        }
    }

}