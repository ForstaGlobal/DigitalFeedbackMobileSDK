package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

public class DefaultLayoutTextBox implements DefaultLayoutItem {
    private DefaultLayoutTextBoxType textBoxType;
    private String initialValue;

    DefaultLayoutTextBox(DefaultLayoutTextBoxType textBoxType, String initialValue) {
        this.textBoxType = textBoxType;
        this.initialValue = initialValue;
    }

    public DefaultLayoutTextBoxType getTextBoxType() {
        return textBoxType;
    }

    public String getInitialValue() {
        return initialValue;
    }
}
