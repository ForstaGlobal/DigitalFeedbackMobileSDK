package com.example.myapp.layouts.defaultlayout.cells

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutItem

interface DefaultLayoutCellChangeListener {
    fun onRadioClicked(answer: QuestionAnswer)
    fun onCheckClicked(answer: QuestionAnswer, selected: Boolean)
    fun getSelected(answer: QuestionAnswer): Boolean
    fun onTextUpdated(text: String)
}

abstract class DefaultLayoutCell(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun setup(item: DefaultLayoutItem, listener: DefaultLayoutCellChangeListener)
}