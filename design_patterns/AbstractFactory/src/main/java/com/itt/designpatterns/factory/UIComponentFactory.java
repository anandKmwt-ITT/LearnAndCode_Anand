package com.itt.designpatterns.factory;

import com.itt.designpatterns.products.Button;
import com.itt.designpatterns.products.Checkbox;
import com.itt.designpatterns.products.TextField;

public interface UIComponentFactory {

    Button createButton();
    Checkbox createCheckbox();
    TextField createTextField();
}
