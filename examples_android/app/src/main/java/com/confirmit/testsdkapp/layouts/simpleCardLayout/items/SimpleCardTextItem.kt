package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.View
import android.widget.EditText
import com.confirmit.mobilesdk.ui.questions.TextQuestion
import com.confirmit.testsdkapp.R

class SimpleCardTextItem(view: View) : SimpleCardItem(view) {

    private lateinit var question: TextQuestion
    private val txtContent: EditText = view.findViewById(R.id.editText)

    fun setupQuestion(question: TextQuestion) {
        this.question = question
        txtHeader.text = question.text.get()
        txtContent.setText(question.getValue())

        handleValidation(question.errors)
    }

    override fun onShow(): Boolean {
        txtContent.requestFocus()
        return false
    }

    override fun onNext(): Boolean {
        question.setValue(txtContent.text.toString())
        return validate(question)
    }

    override fun onBack(): Boolean {
        question.setValue(txtContent.text.toString())
        return true
    }
}