package com.example.myapp.layouts.defaultlayout.cells

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.myapp.R
import com.example.myapp.layouts.defaultlayout.items.DefaultLabelType
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutItem
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutLabel

class DefaultLayoutLabelCell(view: View) : DefaultLayoutCell(view) {

    private lateinit var item: DefaultLayoutLabel
    private val lblText: TextView = view.findViewById(R.id.txtLabel)
    private val container: ConstraintLayout = view.findViewById(R.id.container)

    override fun setup(item: DefaultLayoutItem, listener: DefaultLayoutCellChangeListener) {
        this.item = item as DefaultLayoutLabel

        val density = lblText.resources.displayMetrics.density
        val set = ConstraintSet()
        set.clone(container)
        set.setMargin(lblText.id, ConstraintSet.START, (density * 24 + item.indent * (density * 24)).toInt())
        set.applyTo(container)

        when (item.labelType) {
            DefaultLabelType.TEXT -> {
                lblText.setTypeface(null, Typeface.NORMAL)
                lblText.textSize = 16.0f
                lblText.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }
            DefaultLabelType.INSTRUCTION -> {
                lblText.setTypeface(null, Typeface.NORMAL)
                lblText.textSize = 14.0f
                lblText.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }
            DefaultLabelType.PAGE_TITLE -> {
                lblText.setTypeface(null, Typeface.BOLD)
                lblText.textSize = 16.0f
                lblText.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }

        if (item.value.hasStyle) {
            lblText.text = item.value.getSpanned()
        } else {
            lblText.text = item.value.get()
        }
    }
}
