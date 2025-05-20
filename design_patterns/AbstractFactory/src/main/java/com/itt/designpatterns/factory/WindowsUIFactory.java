package com.itt.designpatterns.factory;

import com.itt.designpatterns.products.*;

public class WindowsUIFactory implements UIComponentFactory{
    @Override
    public Button createButton() {
        return new WindowsButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }

    @Override
    public TextField createTextField() {
        return new WindowsTextField();
    }
}
