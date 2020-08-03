package com.confirmit.testsdkapp.layouts.simplecardlayout.items

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.DefaultQuestion
import com.confirmit.testsdkapp.R

interface SimpleCardItemClickListeners {
    fun onBackClicked(index: Int)
    fun onNextClicked(index: Int)
}

abstract class SimpleCardItem(private val view: View) {

    private var btnBack: Button = view.findViewById(R.id.btnBack)
    private var btnNext: Button = view.findViewById(R.id.btnNext)
    private var txtValidation: TextView = view.findViewById(R.id.txtValidation)
    private var containerRoot: ConstraintLayout = view.findViewById(R.id.containerRoot)
    protected var txtHeader: TextView = view.findViewById(R.id.txtHeader)

    private var index: Int = -1
    private var isFirst: Boolean = false
    private var isLast: Boolean = false

    var listener: SimpleCardItemClickListeners? = null

    init {

        btnBack.setOnClickListener {
            if (onBack()) {
                listener?.onBackClicked(index)
            }
        }

        btnNext.setOnClickListener {
            if (onNext()) {
                listener?.onNextClicked(index)
            }
        }
    }

    fun setupItem(index: Int, showBack: Boolean, showNext: Boolean, isFirst: Boolean, isLast: Boolean) {
        this.index = index
        btnBack.visibility = if (showBack) Button.VISIBLE else Button.INVISIBLE
        btnNext.visibility = if (showNext) Button.VISIBLE else Button.INVISIBLE
        this.isFirst = isFirst
        this.isLast = isLast
    }

    fun handleValidation(errors: List<ValidationQuestionError>) {
        if (errors.isNotEmpty()) {
            txtValidation.text = "Please verify submitted answer"
            txtValidation.visibility = ConstraintLayout.VISIBLE
            containerRoot.background = ContextCompat.getDrawable(view.context, R.drawable.simple_card_border_error)
            containerRoot.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.shake))
            txtHeader.background = ContextCompat.getDrawable(view.context, R.drawable.simple_card_header_border_error)
        } else {
            txtValidation.text = ""
            txtValidation.visibility = ConstraintLayout.INVISIBLE
            containerRoot.background = ContextCompat.getDrawable(view.context, R.drawable.simple_card_border)
            txtHeader.background = ContextCompat.getDrawable(view.context, R.drawable.simple_card_header_border)
        }
    }

    fun validate(question: DefaultQuestion): Boolean {
        try {
            val errors = question.validate()
            if (errors.isNotEmpty()) {
                handleValidation(errors)
                return false
            }
        } catch (e: Exception) {
            return false
        }
        handleValidation(mutableListOf())
        return true
    }

    open fun onBack(): Boolean {
        return true
    }

    open fun onNext(): Boolean {
        return true
    }

    open fun onShow(): Boolean {
        return true
    }

    fun getView(): View {
        return view
    }
}