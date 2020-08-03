package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.SurveyPage;
import com.confirmit.mobilesdk.ui.questions.DefaultQuestion;
import com.confirmit.mobilesdk.ui.questions.InfoQuestion;
import com.confirmit.mobilesdk.ui.questions.MultiQuestion;
import com.confirmit.mobilesdk.ui.questions.NumericQuestion;
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;
import com.confirmit.mobilesdk.ui.questions.QuestionText;
import com.confirmit.mobilesdk.ui.questions.SingleQuestion;
import com.confirmit.mobilesdk.ui.questions.TextQuestion;
import com.confirmit.testsdkapp.R;

import java.util.ArrayList;
import java.util.List;

class DefaultLayoutItemFactory {
    private static List<DefaultLayoutItem> buildCommon(DefaultQuestion question) {
        List<DefaultLayoutItem> result = new ArrayList<>();
        if (!question.getTitle().isEmpty()) {
            result.add(new DefaultLayoutIconLabel(question.getTitle(), R.drawable.ic_assignment_black));
        }
        if (!question.getText().isEmpty()) {
            result.add(new DefaultLayoutLabel(DefaultLabelType.TEXT, question.getText()));
        }
        if (!question.getInstruction().isEmpty()) {
            result.add(new DefaultLayoutLabel(DefaultLabelType.INSTRUCTION, question.getInstruction()));
        }
        if (!question.getErrors().isEmpty()) {
            result.add(new DefaultLayoutIconLabel(question.getErrors()));
        }

        return result;
    }

    static List<DefaultLayoutItem> createPageHeader(SurveyPage page) {
        List<DefaultLayoutItem> result = new ArrayList<>();
        if (!page.getTitle().isEmpty()) {
            result.add(new DefaultLayoutLabel(DefaultLabelType.PAGE_TITLE, page.getTitle()));
        }

        return result;
    }

    static List<DefaultLayoutItem> createInfo(InfoQuestion question) {
        List<DefaultLayoutItem> result = new ArrayList<>();
        if (!question.getTitle().isEmpty()) {
            result.add(new DefaultLayoutIconLabel(question.getTitle(), R.drawable.ic_assignment_black));
        }

        if (!question.getText().isEmpty()) {
            result.add(new DefaultLayoutLabel(DefaultLabelType.TEXT, question.getText()));
        }

        if (!question.getInstruction().isEmpty()) {
            result.add(new DefaultLayoutLabel(DefaultLabelType.INSTRUCTION, question.getInstruction()));
        }
        return result;
    }

    static List<DefaultLayoutItem> createText(TextQuestion question) {
        List<DefaultLayoutItem> result = buildCommon(question);
        result.add(new DefaultLayoutTextBox(DefaultLayoutTextBoxType.TEXT, question.getValue()));
        return result;
    }

    static List<DefaultLayoutItem> createNumeric(NumericQuestion question) {
        List<DefaultLayoutItem> result = buildCommon(question);
        result.add(new DefaultLayoutTextBox(DefaultLayoutTextBoxType.NUMERIC, question.getValue()));
        return result;
    }

    static List<DefaultLayoutItem> createNotSupported() {
        List<DefaultLayoutItem> result = new ArrayList<>();
        result.add(new DefaultLayoutIconLabel(new QuestionText("Question is not supported", false), R.drawable.ic_info_black));
        return result;
    }

    static List<DefaultLayoutItem> createSingle(SingleQuestion question) {
        List<DefaultLayoutItem> result = buildCommon(question);

        String selectedCode = "";
        if (question.selected() != null) {
            selectedCode = question.selected().getCode();
        }

        for (QuestionAnswer answer : question.getAnswers()) {
            if (answer.isHeader()) {
                if (!answer.getText().isEmpty()) {
                    result.add(new DefaultLayoutLabel(DefaultLabelType.TEXT, answer.getText(), 1));
                }
            } else {
                result.add(new DefaultLayoutClickLabel(DefaultLayoutClickLabelType.RADIO, selectedCode == answer.getCode(), 1, answer));
            }

            for (QuestionAnswer nestedAnswer : answer.getAnswers()) {
                result.add(new DefaultLayoutClickLabel(DefaultLayoutClickLabelType.RADIO, selectedCode == nestedAnswer.getCode(), 2, nestedAnswer));
            }
        }

        return result;
    }

    static List<DefaultLayoutItem> createMulti(MultiQuestion question) {
        List<DefaultLayoutItem> result = buildCommon(question);

        for (QuestionAnswer answer : question.getAnswers()) {
            if (answer.isHeader()) {
                if (!answer.getText().isEmpty()) {
                    result.add(new DefaultLayoutLabel(DefaultLabelType.TEXT, answer.getText(), 1));
                }
            } else {
                result.add(new DefaultLayoutClickLabel(DefaultLayoutClickLabelType.CHECKBOX, question.get(answer), 1, answer));
            }

            for (QuestionAnswer nestedAnswer : answer.getAnswers()) {
                result.add(new DefaultLayoutClickLabel(DefaultLayoutClickLabelType.CHECKBOX, question.get(nestedAnswer), 2, nestedAnswer));
            }
        }

        return result;
    }
}
