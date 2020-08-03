package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

public enum DefaultSurveyCellType {
    PAGE_HEADER(0),
    LABEL(1),
    ICON_LABEL(2),
    TEXTBOX(3),
    CLICK_LABEL(4);

    private int value;

    DefaultSurveyCellType(int value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public static DefaultSurveyCellType fromValue(int value) {
        for (DefaultSurveyCellType current : DefaultSurveyCellType.values()) {
            if (current.intValue() == value) {
                return current;
            }
        }

        return null;
    }
}