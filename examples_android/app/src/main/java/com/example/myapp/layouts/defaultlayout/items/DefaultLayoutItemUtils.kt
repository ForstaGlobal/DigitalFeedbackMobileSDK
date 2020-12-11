package com.example.myapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.questions.DefaultQuestion
import com.confirmit.mobilesdk.ui.questions.SingleQuestion

class IndexPath(var row: Int, var section: Int)

object DefaultLayoutItemUtils {
    fun getSingleAnswerIndexList(section: Int, question: SingleQuestion, excludeCode: String): List<IndexPath> {
        var startIndex = 0
        if (!question.title.isEmpty()) startIndex += 1
        if (!question.text.isEmpty()) startIndex += 1
        if (!question.instruction.isEmpty()) startIndex += 1
        if (question.errors.isNotEmpty()) startIndex += 1

        val result = mutableListOf<IndexPath>()
        question.answers.forEach { answer ->
            if (!answer.isHeader && answer.code != excludeCode) {
                result.add(IndexPath(startIndex, section))
            }

            if (answer.isHeader) {
                if (!answer.text.isEmpty()) {
                    startIndex += 1
                }
            } else {
                startIndex += 1
            }

            for (nestedAnswer in answer.answers) {
                if (answer.code != excludeCode) {
                    result.add(IndexPath(startIndex, section))
                }

                startIndex += 1
            }
        }

        return result
    }

    fun getValidationIndex(section: Int, question: DefaultQuestion): IndexPath {
        var startIndex = 0
        if (!question.title.isEmpty()) startIndex += 1
        if (!question.text.isEmpty()) startIndex += 1
        if (!question.instruction.isEmpty()) startIndex += 1
        return IndexPath(startIndex, section)
    }
}