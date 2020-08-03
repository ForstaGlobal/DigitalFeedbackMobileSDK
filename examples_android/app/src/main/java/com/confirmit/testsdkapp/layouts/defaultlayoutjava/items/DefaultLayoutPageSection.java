package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.SurveyPage;
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;

import java.util.List;

public class DefaultLayoutPageSection implements DefaultLayoutSection {

    private List<DefaultLayoutItem> items;

    public DefaultLayoutPageSection(SurveyPage page) {
        items = DefaultLayoutItemFactory.createPageHeader(page);
    }

    @Override
    public void onRadioClicked(QuestionAnswer answer) {
    }

    @Override
    public void onCheckClicked(QuestionAnswer answer, Boolean selected) {
    }

    @Override
    public boolean getSelected(QuestionAnswer answer) {
        return false;
    }

    @Override
    public void onTextUpdate(String text) {
    }

    @Override
    public List<DefaultLayoutItem> getItems() {
        return items;
    }

    @Override
    public void load(DefaultLayoutSectionListener listener, int section) {
    }
}
