package com.itt.designpatterns.factory;

import com.itt.designpatterns.service.BikeService;
import com.itt.designpatterns.service.CarService;
import com.itt.designpatterns.service.TruckService;
import com.itt.designpatterns.service.VehicleService;

public class VehicleServiceFactory {

    public static VehicleService getVehicleService(String vehicleType) {
        if(vehicleType == null){
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        switch (vehicleType.toLowerCase()) {
            case "car":
                return new CarService();
            case "bike":
                return new BikeService();
            case "truck":
                return new TruckService();
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}
