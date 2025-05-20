package com.itt.designpatterns.service;

public class BikeService implements VehicleService{
    @Override
    public void performService() {
        System.out.println("Performing Bike Service: Chain lubrication, Brake tightening");
    }
}
