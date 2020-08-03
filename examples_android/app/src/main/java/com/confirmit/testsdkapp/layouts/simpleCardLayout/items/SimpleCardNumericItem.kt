package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.View
import android.widget.EditText
import com.confirmit.mobilesdk.ui.questions.NumericQuestion
import com.confirmit.testsdkapp.R

class SimpleCardNumericItem(view: View) : SimpleCardItem(view) {

    private val txtNumeric: EditText = view.findViewById(R.id.editText)
    private lateinit var question: NumericQuestion

    fun setupQuestion(question: NumericQuestion) {
        this.question = question
        txtHeader.text = question.text.get()
        txtNumeric.setText(question.getValue())

        handleValidation(question.errors)
    }

    override fun onShow(): Boolean {
        txtNumeric.requestFocus()
        return false
    }

    override fun onNext(): Boolean {
        txtNumeric.text.toString().toDoubleOrNull()?.let {
            question.setValue(it)
        }

        return validate(question)
    }

    override fun onBack(): Boolean {
        txtNumeric.text.toString().toDoubleOrNull()?.let {
            question.setValue(it)
        }

        return true
    }
}