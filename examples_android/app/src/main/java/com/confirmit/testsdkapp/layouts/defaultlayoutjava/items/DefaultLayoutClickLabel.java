package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;

public class DefaultLayoutClickLabel implements DefaultLayoutItem {
    private DefaultLayoutClickLabelType clickLabelType;
    private Boolean initial;
    private int indent;
    private QuestionAnswer answer;

    DefaultLayoutClickLabel(DefaultLayoutClickLabelType clickLabelType, Boolean initial, int indent, QuestionAnswer answer) {
        this.clickLabelType = clickLabelType;
        this.initial = initial;
        this.indent = indent;
        this.answer = answer;
    }

    public Boolean getInitial() {
        return initial;
    }

    public DefaultLayoutClickLabelType getClickLabelType() {
        return clickLabelType;
    }

    public int getIndent() {
        return indent;
    }

    public QuestionAnswer getAnswer() {
        return answer;
    }
}
