package com.example.myapp.layouts.defaultlayout.cells

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.myapp.R
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutItem
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutTextBox
import com.example.myapp.layouts.defaultlayout.items.DefaultLayoutTextBoxType

class DefaultLayoutTextBoxCell(view: View) : DefaultLayoutCell(view), TextWatcher {

    private lateinit var listener: DefaultLayoutCellChangeListener
    private lateinit var item: DefaultLayoutTextBox
    private val viewId: Int = view.id
    private val txtText: EditText = view.findViewById(R.id.editText)
    private val container: ConstraintLayout = view.findViewById(R.id.constraintEditText)

    init {
        txtText.setBackgroundResource(R.drawable.round_corner_survey_textedit)
        txtText.addTextChangedListener(this)
    }

    override fun setup(item: DefaultLayoutItem, listener: DefaultLayoutCellChangeListener) {
        this.item = item as DefaultLayoutTextBox
        this.listener = listener
        txtText.clearFocus()
        txtText.setText(item.initialValue)

        val density = txtText.resources.displayMetrics.density
        val set = ConstraintSet()
        set.clone(container)
        set.connect(viewId, ConstraintSet.LEFT, container.id, ConstraintSet.LEFT, (density * 24).toInt())
        set.connect(viewId, ConstraintSet.RIGHT, container.id, ConstraintSet.RIGHT, (density * 24).toInt())

        when (item.textBoxType) {
            DefaultLayoutTextBoxType.TEXT -> {
                txtText.setRawInputType(InputType.TYPE_CLASS_TEXT)
                set.constrainHeight(R.id.editText, (density * 120).toInt())
            }
            DefaultLayoutTextBoxType.NUMERIC -> {
                txtText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
                set.constrainHeight(R.id.editText, (density * 50).toInt())
            }
        }

        set.applyTo(container)
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (!txtText.hasFocus()) {
            return
        }

        listener.onTextUpdated(s.toString())
    }
}
