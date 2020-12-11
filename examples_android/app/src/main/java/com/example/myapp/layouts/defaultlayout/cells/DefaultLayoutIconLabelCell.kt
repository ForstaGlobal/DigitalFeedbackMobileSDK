package com.example.myapp.layouts.defaultlayout.cells

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.example.myapp.R
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutIconLabel
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutItem

class DefaultLayoutIconLabelCell(view: View) : DefaultLayoutCell(view) {

    private lateinit var item: DefaultLayoutIconLabel
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
    private val lblText: TextView = view.findViewById(R.id.txtLabel)

    override fun setup(item: DefaultLayoutItem, listener: DefaultLayoutCellChangeListener) {
        this.item = item as DefaultLayoutIconLabel

        val context = imgIcon.context
        imgIcon.setImageResource(item.iconId)
        if (item.errors.isNotEmpty()) {
            lblText.text = getErrorMessage(item.errors)
            lblText.setTextColor(ContextCompat.getColor(context, R.color.defaultSurveyError))
            imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyError))
        } else {
            imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyFont))
            lblText.setTextColor(ContextCompat.getColor(context, R.color.defaultSurveyFont))

            if (item.value.hasStyle) {
                lblText.text = item.value.getSpanned()
            } else {
                lblText.text = item.value.get()
            }
        }
    }

    private fun getErrorMessage(errors: List<ValidationQuestionError>): String {
        var result = ""
        errors.forEach {
            result += it.message + "\n"
        }

        return result.dropLast(1)
    }
}