package com.example.myapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.QuestionText
import com.example.myapp.R

class DefaultLayoutIconLabel : DefaultLayoutItem {
    val value: QuestionText
    val iconId: Int
    var errors: List<ValidationQuestionError>

    constructor(value: QuestionText, iconId: Int) {
        this.value = value
        this.iconId = iconId
        this.errors = mutableListOf()
    }

    constructor(errors: List<ValidationQuestionError>) {
        this.value = QuestionText("", false)
        this.iconId = R.drawable.ic_sentiment_very_dissatisfied
        this.errors = errors
    }
}