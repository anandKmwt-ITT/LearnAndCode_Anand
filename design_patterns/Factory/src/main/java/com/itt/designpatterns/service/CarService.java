package com.itt.designpatterns.service;

public class CarService implements VehicleService{
    @Override
    public void performService() {
        System.out.println("Performing Car Service: Oil change, Brake inspection, Tire rotation");
    }
}
