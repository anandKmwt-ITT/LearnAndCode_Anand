package com.itt.designpatterns.products;

public class MacOSButton implements Button{
    @Override
    public void render() {
        System.out.println("Rendering MacOS-style Button");
    }
}
