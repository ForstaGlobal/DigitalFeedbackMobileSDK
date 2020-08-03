package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.questions.QuestionText;

public class DefaultLayoutLabel implements DefaultLayoutItem {
    private DefaultLabelType labelType;
    private QuestionText value;
    private int indent;

    DefaultLayoutLabel(DefaultLabelType labelType, QuestionText value) {
        this.labelType = labelType;
        this.value = value;
        indent = 0;
    }

    DefaultLayoutLabel(DefaultLabelType labelType, QuestionText value, int indent) {
        this.labelType = labelType;
        this.value = value;
        this.indent = indent;
    }

    public QuestionText getValue() {
        return value;
    }

    public int getIndent() {
        return indent;
    }

    public DefaultLabelType getLabelType() {
        return labelType;
    }
}
