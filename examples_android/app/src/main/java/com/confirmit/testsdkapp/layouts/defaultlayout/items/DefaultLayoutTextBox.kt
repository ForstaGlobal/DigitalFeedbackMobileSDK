package com.confirmit.testsdkapp.layouts.defaultlayout.items

enum class DefaultLayoutTextBoxType {
    TEXT,
    NUMERIC
}

class DefaultLayoutTextBox(val textBoxType: DefaultLayoutTextBoxType, val initialValue: String) : DefaultLayoutItem