package com.itt.designpatterns.products;

public class MacOSTextField implements TextField{
    @Override
    public void render() {
        System.out.println("Rendering MacOS-style TextField");
    }
}
