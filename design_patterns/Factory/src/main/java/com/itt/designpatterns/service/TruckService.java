package com.itt.designpatterns.service;

public class TruckService implements VehicleService{
    @Override
    public void performService() {
        System.out.println("Performing Truck Service: Engine diagnostics, Cargo inspection");
    }
}
