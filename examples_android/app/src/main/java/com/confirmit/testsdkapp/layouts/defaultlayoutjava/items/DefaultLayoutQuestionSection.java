package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.mobilesdk.ui.ValidationQuestionError;
import com.confirmit.mobilesdk.ui.questions.DefaultQuestion;
import com.confirmit.mobilesdk.ui.questions.InfoQuestion;
import com.confirmit.mobilesdk.ui.questions.MultiQuestion;
import com.confirmit.mobilesdk.ui.questions.NumericQuestion;
import com.confirmit.mobilesdk.ui.questions.Question;
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer;
import com.confirmit.mobilesdk.ui.questions.SingleQuestion;
import com.confirmit.mobilesdk.ui.questions.TextQuestion;
import com.confirmit.testsdkapp.AppConfigs;

import java.util.ArrayList;
import java.util.List;

public class DefaultLayoutQuestionSection implements DefaultLayoutSection {
    private DefaultLayoutSectionListener listener;
    private int section;

    private List<DefaultLayoutItem> items;
    private Question question;

    public DefaultLayoutQuestionSection(Question question) {
        items = createItems(question);
        this.question = question;
    }

    private List<DefaultLayoutItem> createItems(Question question) {
        List<DefaultLayoutItem> items;
        if (question instanceof InfoQuestion) {
            items = DefaultLayoutItemFactory.createInfo((InfoQuestion) question);
        } else if (question instanceof TextQuestion) {
            items = DefaultLayoutItemFactory.createText((TextQuestion) question);
        } else if (question instanceof NumericQuestion) {
            items = DefaultLayoutItemFactory.createNumeric((NumericQuestion) question);
        } else if (question instanceof SingleQuestion) {
            items = DefaultLayoutItemFactory.createSingle((SingleQuestion) question);
        } else if (question instanceof MultiQuestion) {
            items = DefaultLayoutItemFactory.createMulti((MultiQuestion) question);
        } else {
            items = DefaultLayoutItemFactory.createNotSupported();
        }

        return items;
    }

    private void updateValidation(DefaultQuestion question, List<ValidationQuestionError> errors) {
        final IndexPath validationIndex = DefaultLayoutItemUtils.getValidationIndex(section, question);
        DefaultLayoutItem item = items.get(validationIndex.getRow());
        if (item == null) {
            return;
        }

        if (!(item instanceof DefaultLayoutIconLabel)) {
            if (!errors.isEmpty()) {
                items.add(validationIndex.getRow(), new DefaultLayoutIconLabel(errors));
                if (listener != null) {
                    listener.onReloadPage(
                            new ArrayList<IndexPath>() {{
                                add(validationIndex);
                            }},
                            new ArrayList<IndexPath>(),
                            new ArrayList<IndexPath>());
                }
            }
            return;
        }

        DefaultLayoutIconLabel iconLabelItem = (DefaultLayoutIconLabel) item;

        List<IndexPath> addRow = new ArrayList<>();
        List<IndexPath> removeRow = new ArrayList<>();
        List<IndexPath> refreshRow = new ArrayList<>();

        if (!iconLabelItem.getErrors().isEmpty()) {
            if (errors.isEmpty()) {
                items.remove(validationIndex.getRow());
                removeRow.add(validationIndex);
            } else {
                iconLabelItem.setErrors(errors);
                refreshRow.add(validationIndex);
            }
        } else {
            items.add(validationIndex.getRow(), new DefaultLayoutIconLabel(errors));
            addRow.add(validationIndex);
        }

        if (listener != null) {
            listener.onReloadPage(addRow, refreshRow, removeRow);
        }
    }

    @Override
    public void onRadioClicked(QuestionAnswer answer) {
        if (question instanceof SingleQuestion) {
            ((SingleQuestion) question).select(answer);

            validate((SingleQuestion) question);

            if (listener != null) {
                listener.onReloadPage(new ArrayList<IndexPath>(),
                        DefaultLayoutItemUtils.getSingleAnswerIndexList(section, (SingleQuestion) question, answer.getCode()),
                        new ArrayList<IndexPath>());
            }
        }
    }

    @Override
    public void onCheckClicked(QuestionAnswer answer, Boolean selected) {
        if (question instanceof MultiQuestion) {
            ((MultiQuestion) question).set(answer, selected);
            validate((MultiQuestion) question);
        }
    }

    @Override
    public boolean getSelected(QuestionAnswer answer) {
        if (question instanceof SingleQuestion) {
            QuestionAnswer selected = ((SingleQuestion) question).selected();
            if (selected != null) {
                return selected.getCode().equals(answer.getCode());
            }
            return false;
        }

        if (question instanceof MultiQuestion) {
            return ((MultiQuestion) question).get(answer);
        }

        return false;
    }

    @Override
    public void onTextUpdate(String text) {
        if (question instanceof TextQuestion) {
            TextQuestion textQuestion = (TextQuestion) question;
            if (!text.equals(textQuestion.getValue())) {
                textQuestion.setValue(text);
                validate(textQuestion);
            }
        }

        if (question instanceof NumericQuestion) {
            NumericQuestion numericQuestion = (NumericQuestion) question;
            try {
                double parsed = Double.parseDouble(text);
                numericQuestion.setValue(parsed);
            } catch (NumberFormatException ignored) {
                numericQuestion.setValue((Double) null);
            }
            validate(numericQuestion);
        }
    }

    private void validate(DefaultQuestion question) {
        if (AppConfigs.INSTANCE.getOnPageQuestionValidation()) {
            List<ValidationQuestionError> errors = question.validate();
            updateValidation(question, errors);
        }
    }

    @Override
    public List<DefaultLayoutItem> getItems() {
        return items;
    }

    @Override
    public void load(DefaultLayoutSectionListener listener, int section) {
        this.listener = listener;
        this.section = section;
    }
}
