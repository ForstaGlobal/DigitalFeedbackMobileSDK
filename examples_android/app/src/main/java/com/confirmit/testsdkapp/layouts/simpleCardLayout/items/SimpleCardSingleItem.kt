package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer
import com.confirmit.mobilesdk.ui.questions.SingleQuestion
import com.confirmit.testsdkapp.R

class SimpleCardSingleItem(view: View) : SimpleCardItem(view), SimpleCardSelectedItemClickedListener {

    private lateinit var question: SingleQuestion

    private val listView: LinearLayout = view.findViewById(R.id.ctnList)
    private val itemList: MutableList<SimpleCardSelectedItem> = mutableListOf()

    fun setupQuestion(question: SingleQuestion) {
        this.question = question
        txtHeader.text = question.text.get()

        createAnswers(question)

        handleValidation(question.errors)
    }

    private fun createAnswers(question: SingleQuestion) {
        listView.removeAllViews()
        itemList.clear()
        val selectedAnswer = question.selected()
        question.answers.forEach { questionAnswer ->
            val view = LayoutInflater.from(getView().context).inflate(R.layout.simple_card_select_item, listView, false)
            val selectItem = SimpleCardSelectedItem(view)
            selectItem.setupItem(false, questionAnswer, questionAnswer.code == selectedAnswer?.code)
            selectItem.listener = this
            itemList.add(selectItem)
            listView.addView(view)
        }
    }

    override fun onNext(): Boolean {
        return validate(question)
    }

    override fun onBack(): Boolean {
        return true
    }

    override fun onRadioClicked(answer: QuestionAnswer) {
        question.select(answer)
        for (item in itemList) {
            item.updateSelected(answer.code == item.answer.code)
        }
    }

    override fun onCheckClicked(answer: QuestionAnswer, selected: Boolean) {
    }
}