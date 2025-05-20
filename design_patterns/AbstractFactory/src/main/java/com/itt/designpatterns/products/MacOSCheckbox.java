package com.itt.designpatterns.products;

public class MacOSCheckbox implements Checkbox{
    @Override
    public void render() {
        System.out.println("Rendering MacOS-style Checkbox");
    }
}
