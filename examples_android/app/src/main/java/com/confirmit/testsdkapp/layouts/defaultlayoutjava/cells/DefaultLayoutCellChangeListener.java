package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;

public interface DefaultLayoutCellChangeListener {
    void onRadioClicked(QuestionAnswer answer);

    void onCheckClicked(QuestionAnswer answer, Boolean selected);

    boolean getSelected(QuestionAnswer answer);

    void onTextUpdate(String text);
}
