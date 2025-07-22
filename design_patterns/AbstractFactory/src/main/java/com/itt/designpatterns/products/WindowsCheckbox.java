package com.itt.designpatterns.products;

public class WindowsCheckbox implements Checkbox{
    @Override
    public void render() {
        System.out.println("Rendering Windows-style Checkbox");
    }
}
