package com.itt.designpatterns.app;

import com.itt.designpatterns.factory.MacOSUIFactory;
import com.itt.designpatterns.factory.UIComponentFactory;
import com.itt.designpatterns.factory.WindowsUIFactory;

public class Main {
    public static void main(String[] args) {

        String osType = "mac";

        UIComponentFactory factory;

        if ("windows".equalsIgnoreCase(osType)) {
            factory = new WindowsUIFactory();
        } else if ("mac".equalsIgnoreCase(osType)) {
            factory = new MacOSUIFactory();
        } else {
            throw new RuntimeException("Unsupported OS type: " + osType);
        }

        Application app = new Application(factory);
        app.renderUI();
    }
}
