package com.confirmit.testsdkapp.layouts.defaultlayout.cells

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.layouts.defaultlayout.items.DefaultLayoutClickLabel
import com.confirmit.testsdkapp.layouts.defaultlayout.items.DefaultLayoutClickLabelType
import com.confirmit.testsdkapp.layouts.defaultlayout.items.DefaultLayoutItem

class DefaultLayoutClickLabelCell(view: View) : DefaultLayoutCell(view), View.OnClickListener {

    private lateinit var listener: DefaultLayoutCellChangeListener
    private lateinit var item: DefaultLayoutClickLabel
    private var itemSelected: Boolean = false

    private val btnContainer: ConstraintLayout = view.findViewById(R.id.containerButton)
    private val lblText: TextView = view.findViewById(R.id.txtLabel)
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

    init {
        btnContainer.setOnClickListener(this)
    }

    override fun setup(item: DefaultLayoutItem, listener: DefaultLayoutCellChangeListener) {
        this.item = item as DefaultLayoutClickLabel
        this.listener = listener

        if (item.answer.text.hasStyle && AppConfigs.htmlText) {
            lblText.text = item.answer.text.getSpanned()
        } else {
            lblText.text = item.answer.text.get()
        }

        val density = imgIcon.resources.displayMetrics.density
        val set = ConstraintSet()
        set.clone(btnContainer)
        set.setMargin(imgIcon.id, ConstraintSet.START, (density * 24 + item.indent * (density * 24)).toInt())
        set.applyTo(btnContainer)

        itemSelected = listener.getSelected(item.answer)

        setIcon()
    }

    override fun onClick(view: View?) {
        if (itemSelected && item.clickLabelType == DefaultLayoutClickLabelType.RADIO) {
            return
        }

        itemSelected = !itemSelected
        setIcon()
        when (item.clickLabelType) {
            DefaultLayoutClickLabelType.CHECKBOX -> listener.onCheckClicked(item.answer, itemSelected)
            DefaultLayoutClickLabelType.RADIO -> listener.onRadioClicked(item.answer)
        }
    }

    private fun setIcon() {
        val imageId = when (item.clickLabelType) {
            DefaultLayoutClickLabelType.CHECKBOX -> {
                if (itemSelected) R.drawable.ic_outline_check_black else R.drawable.ic_outline_box_black
            }
            DefaultLayoutClickLabelType.RADIO -> {
                if (itemSelected) R.drawable.ic_radio_black else R.drawable.ic_radio_unchecked_black
            }
        }

        imgIcon.setImageResource(imageId)
        imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, if (itemSelected) R.color.defaultSurveyButton else R.color.font))
    }
}
