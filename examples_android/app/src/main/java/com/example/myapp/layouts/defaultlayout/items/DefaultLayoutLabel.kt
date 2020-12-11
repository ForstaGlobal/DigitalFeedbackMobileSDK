package com.example.myapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.questions.QuestionText

enum class DefaultLabelType {
    TEXT,
    INSTRUCTION,
    PAGE_TITLE
}

class DefaultLayoutLabel(val labelType: DefaultLabelType, val value: QuestionText, val indent: Int = 0) : DefaultLayoutItem