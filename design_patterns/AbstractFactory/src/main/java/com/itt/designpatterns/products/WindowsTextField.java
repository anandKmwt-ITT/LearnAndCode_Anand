package com.itt.designpatterns.products;

public class WindowsTextField implements TextField{
    @Override
    public void render() {
        System.out.println("Rendering Windows-style TextField");
    }
}
