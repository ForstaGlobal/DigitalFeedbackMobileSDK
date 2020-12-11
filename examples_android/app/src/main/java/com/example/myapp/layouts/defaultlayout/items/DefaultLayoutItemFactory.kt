package com.example.myapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.SurveyPage
import com.confirmit.mobilesdk.ui.questions.*
import com.example.myapp.R

object DefaultLayoutItemFactory {
    private fun buildCommon(question: DefaultQuestion): MutableList<DefaultLayoutItem> {
        val result = mutableListOf<DefaultLayoutItem>()
        if (!question.title.isEmpty()) {
            result.add(DefaultLayoutIconLabel(question.title, R.drawable.ic_assignment_black))
        }
        if (!question.text.isEmpty()) {
            result.add(DefaultLayoutLabel(DefaultLabelType.TEXT, question.text))
        }
        if (!question.instruction.isEmpty()) {
            result.add(DefaultLayoutLabel(DefaultLabelType.INSTRUCTION, question.instruction))
        }
        if (question.errors.isNotEmpty()) {
            result.add(DefaultLayoutIconLabel(question.errors))
        }

        return result
    }

    fun createPageHeader(page: SurveyPage): MutableList<DefaultLayoutItem> {
        val result = mutableListOf<DefaultLayoutItem>()
        if (!page.title.isEmpty()) {
            result.add(DefaultLayoutLabel(DefaultLabelType.PAGE_TITLE, page.title))
        }

        return result
    }

    fun createInfo(question: InfoQuestion): MutableList<DefaultLayoutItem> {
        val result = mutableListOf<DefaultLayoutItem>()
        if (!question.title.isEmpty()) {
            result.add(DefaultLayoutIconLabel(question.title, R.drawable.ic_assignment_black))
        }

        if (!question.text.isEmpty()) {
            result.add(DefaultLayoutLabel(DefaultLabelType.TEXT, question.text))
        }

        if (!question.instruction.isEmpty()) {
            result.add(DefaultLayoutLabel(DefaultLabelType.INSTRUCTION, question.instruction))
        }

        return result
    }

    fun createText(question: TextQuestion): MutableList<DefaultLayoutItem> {
        val result = buildCommon(question)
        result.add(DefaultLayoutTextBox(DefaultLayoutTextBoxType.TEXT, question.getValue() ?: ""))
        return result
    }

    fun createNumeric(question: NumericQuestion): MutableList<DefaultLayoutItem> {
        val result = buildCommon(question)
        result.add(
            DefaultLayoutTextBox(
                DefaultLayoutTextBoxType.NUMERIC, question.getValue()
                    ?: ""
            )
        )
        return result
    }

    fun createNotSupported(): MutableList<DefaultLayoutItem> {
        val result = mutableListOf<DefaultLayoutItem>()
        result.add(DefaultLayoutIconLabel(QuestionText("Question is not supported", false), R.drawable.ic_info_black))
        return result
    }

    fun createSingle(question: SingleQuestion): MutableList<DefaultLayoutItem> {
        val result = buildCommon(question)

        var selectedCode = ""
        question.selected()?.let {
            selectedCode = it.code
        }

        question.answers.forEach {
            if (it.isHeader) {
                if (!it.text.isEmpty()) {
                    result.add(DefaultLayoutLabel(DefaultLabelType.TEXT, it.text, 1))
                }
            } else {
                result.add(DefaultLayoutClickLabel(DefaultLayoutClickLabelType.RADIO, selectedCode == it.code, 1, it))
            }

            // Nested answer always not group header
            it.answers.forEach { nestedAnswer ->
                result.add(DefaultLayoutClickLabel(DefaultLayoutClickLabelType.RADIO, selectedCode == nestedAnswer.code, 2, nestedAnswer))
            }
        }

        return result
    }

    fun createMulti(question: MultiQuestion): MutableList<DefaultLayoutItem> {
        val result = buildCommon(question)

        question.answers.forEach {
            if (it.isHeader) {
                if (!it.text.isEmpty()) {
                    result.add(DefaultLayoutLabel(DefaultLabelType.TEXT, it.text, 1))
                }
            } else {
                result.add(DefaultLayoutClickLabel(DefaultLayoutClickLabelType.CHECKBOX, question.get(it), 1, it))
            }

            it.answers.forEach { nestedAnswer ->
                result.add(DefaultLayoutClickLabel(DefaultLayoutClickLabelType.CHECKBOX, question.get(it), 2, nestedAnswer))
            }
        }

        return result
    }
}