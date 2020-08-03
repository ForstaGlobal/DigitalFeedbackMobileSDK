package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.ValidationQuestionError;
import com.confirmit.mobilesdk.ui.questions.QuestionText;
import com.confirmit.testsdkapp.R;

import java.util.ArrayList;
import java.util.List;

public class DefaultLayoutIconLabel implements DefaultLayoutItem {
    private QuestionText value;
    private int iconId;
    private List<ValidationQuestionError> errors;

    public DefaultLayoutIconLabel(QuestionText value, int iconId) {
        this.value = value;
        this.iconId = iconId;
        this.errors = new ArrayList<>();
    }

    public DefaultLayoutIconLabel(List<ValidationQuestionError> errors) {
        this.value = new QuestionText("", false);
        this.iconId = R.drawable.ic_sentiment_dissatisfied;
        this.errors = errors;
    }

    public QuestionText getValue() {
        return value;
    }

    public int getIconId() {
        return iconId;
    }

    public List<ValidationQuestionError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationQuestionError> errors) {
        this.errors = errors;
    }
}
