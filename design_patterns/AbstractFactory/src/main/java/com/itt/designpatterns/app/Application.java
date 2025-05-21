package com.itt.designpatterns.app;

import com.itt.designpatterns.factory.UIComponentFactory;
import com.itt.designpatterns.products.Button;
import com.itt.designpatterns.products.Checkbox;
import com.itt.designpatterns.products.TextField;

public class Application {
    private final Button button;
    private final Checkbox checkbox;
    private final TextField textField;

    public Application(UIComponentFactory factory) {
        this.button = factory.createButton();
        this.checkbox = factory.createCheckbox();
        this.textField = factory.createTextField();
    }

    public void renderUI() {
        button.render();
        checkbox.render();
        textField.render();
    }
}
