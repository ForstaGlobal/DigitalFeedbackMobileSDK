package com.confirmit.testsdkapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.questions.QuestionAnswer

enum class DefaultLayoutClickLabelType {
    RADIO,
    CHECKBOX
}

class DefaultLayoutClickLabel(val clickLabelType: DefaultLayoutClickLabelType, val initial: Boolean, val indent: Int, val answer: QuestionAnswer) : DefaultLayoutItem