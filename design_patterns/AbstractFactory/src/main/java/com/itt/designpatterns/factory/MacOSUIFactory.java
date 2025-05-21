package com.itt.designpatterns.factory;

import com.itt.designpatterns.products.*;

public class MacOSUIFactory implements UIComponentFactory{
    @Override
    public Button createButton() {
        return new MacOSButton();
    }

    @Override
    public Checkbox createCheckbox() {
        return new MacOSCheckbox();
    }

    @Override
    public TextField createTextField() {
        return new MacOSTextField();
    }
}
