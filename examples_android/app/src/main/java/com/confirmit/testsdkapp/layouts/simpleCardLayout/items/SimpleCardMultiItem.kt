package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.confirmit.mobilesdk.ui.questions.MultiQuestion
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer
import com.confirmit.testsdkapp.R

class SimpleCardMultiItem(view: View) : SimpleCardItem(view), SimpleCardSelectedItemClickedListener {

    private lateinit var question: MultiQuestion

    private val listView: LinearLayout = view.findViewById(R.id.ctnList)
    private val itemList: MutableList<SimpleCardSelectedItem> = mutableListOf()

    fun setupQuestion(question: MultiQuestion) {
        this.question = question
        txtHeader.text = question.text.get()

        createAnswers(question)

        handleValidation(question.errors)
    }

    private fun createAnswers(question: MultiQuestion) {
        listView.removeAllViews()
        itemList.clear()
        question.answers.forEach {
            val view = LayoutInflater.from(getView().context).inflate(R.layout.simple_card_select_item, listView, false)
            val selectItem = SimpleCardSelectedItem(view)
            selectItem.setupItem(true, it, question.get(it))
            selectItem.listener = this
            listView.addView(view)
            itemList.add(selectItem)
        }
    }

    override fun onNext(): Boolean {
        return validate(question)
    }

    override fun onBack(): Boolean {
        return true
    }

    override fun onRadioClicked(answer: QuestionAnswer) {
    }

    override fun onCheckClicked(answer: QuestionAnswer, selected: Boolean) {
        question.set(answer, selected)
    }
}