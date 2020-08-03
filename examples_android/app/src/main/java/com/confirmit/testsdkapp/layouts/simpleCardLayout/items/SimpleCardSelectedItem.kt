package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer
import com.confirmit.testsdkapp.R

interface SimpleCardSelectedItemClickedListener {
    fun onRadioClicked(answer: QuestionAnswer)
    fun onCheckClicked(answer: QuestionAnswer, selected: Boolean)
}

class SimpleCardSelectedItem(view: View) {
    private val txtLabel: TextView = view.findViewById(R.id.txtLabel)
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
    private val ctnButton: ConstraintLayout = view.findViewById(R.id.btn_container)

    private var checkbox: Boolean = false
    private var isAnswerSelected: Boolean = false
    lateinit var answer: QuestionAnswer

    var listener: SimpleCardSelectedItemClickedListener? = null

    init {
        ctnButton.setOnClickListener {
            if (isAnswerSelected && !checkbox) {
                return@setOnClickListener
            }

            isAnswerSelected = !isAnswerSelected
            setIcon()
            if (checkbox) {
                listener?.onCheckClicked(answer, isAnswerSelected)
            } else {
                listener?.onRadioClicked(answer)
            }
        }
    }

    fun setupItem(checkbox: Boolean, answer: QuestionAnswer, selected: Boolean) {
        this.checkbox = checkbox
        this.answer = answer
        this.isAnswerSelected = selected
        txtLabel.text = answer.text.get()
        txtLabel.setTextColor(ContextCompat.getColor(txtLabel.context, R.color.font))

        setIcon()
    }

    private fun setIcon() {
        val imageId = if (checkbox) {
            if (isAnswerSelected) R.drawable.ic_outline_check_black else R.drawable.ic_outline_box_black
        } else {
            if (isAnswerSelected) R.drawable.ic_radio_black else R.drawable.ic_radio_unchecked_black
        }

        imgIcon.setImageResource(imageId)
        imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, if (isAnswerSelected) R.color.simpleSurveyButton else R.color.simpleSurveyFont))
    }

    fun updateSelected(selected: Boolean) {
        this.isAnswerSelected = selected
        setIcon()
    }
}