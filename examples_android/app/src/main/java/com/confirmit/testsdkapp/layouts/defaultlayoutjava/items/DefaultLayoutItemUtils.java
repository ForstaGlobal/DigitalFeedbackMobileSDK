package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.questions.DefaultQuestion;
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;
import com.confirmit.mobilesdk.ui.questions.SingleQuestion;

import java.util.ArrayList;
import java.util.List;

class DefaultLayoutItemUtils {
    static List<IndexPath> getSingleAnswerIndexList(int section, SingleQuestion question, String excludeCode) {
        int startIndex = 0;
        if (!question.getTitle().isEmpty()) startIndex += 1;
        if (!question.getText().isEmpty()) startIndex += 1;
        if (!question.getInstruction().isEmpty()) startIndex += 1;
        if (!question.getErrors().isEmpty()) startIndex += 1;

        List<IndexPath> result = new ArrayList<>();
        for (QuestionAnswer answer : question.getAnswers()) {
            if (!answer.isHeader() && !answer.getCode().equals(excludeCode)) {
                result.add(new IndexPath(startIndex, section));
            }

            if (answer.isHeader()) {
                if (!answer.getText().isEmpty()) {
                    startIndex += 1;
                }
            } else {
                startIndex += 1;
            }

            for (QuestionAnswer ignored : answer.getAnswers()) {
                if (!answer.getCode().equals(excludeCode)) {
                    result.add(new IndexPath(startIndex, section));
                }

                startIndex += 1;
            }
        }

        return result;
    }

    static IndexPath getValidationIndex(int section, DefaultQuestion question) {
        int startIndex = 0;
        if (!question.getTitle().isEmpty()) startIndex += 1;
        if (!question.getText().isEmpty()) startIndex += 1;
        if (!question.getInstruction().isEmpty()) startIndex += 1;
        return new IndexPath(startIndex, section);
    }
}
