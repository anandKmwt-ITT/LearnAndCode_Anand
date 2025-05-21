package com.itt.designpatterns.products;

public class WindowsButton implements Button{
    @Override
    public void render() {
        System.out.println("Rendering Windows-style Button");
    }
}
