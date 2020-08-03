package com.confirmit.testsdkapp.layouts.simplefeedbacklayout.items

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.TextQuestion
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.layouts.simplefeedbacklayout.SimpleFbItem

interface SimpleFbTextItemListener {
    fun onSubmitClicked(question: TextQuestion): List<ValidationQuestionError>
}

class SimpleFbOpenTextItem(private val view: View) : SimpleFbItem {

    private var txtOpen: EditText = view.findViewById(R.id.txtOpen)
    private var txtTitle: TextView = view.findViewById(R.id.txtTitle)
    private var txtInstruction: TextView = view.findViewById(R.id.txtInstruction)
    private var btnSubmit: Button = view.findViewById(R.id.btnSubmit)
    private var txtValidation: TextView = view.findViewById(R.id.txtValidation)

    private lateinit var surveyFrame: SurveyFrame
    private lateinit var question: TextQuestion
    var listener: SimpleFbTextItemListener? = null

    init {
        btnSubmit.setOnClickListener {
            question.setValue(txtOpen.text.toString())

            listener?.onSubmitClicked(question)?.let { validationErrors ->
                if (validationErrors.isNotEmpty()) {
                    txtValidation.text = "Wait! We haven't heard from you yet!"
                }
            }
        }

        txtValidation.text = ""
    }

    override fun getView(): View {
        return view
    }

    fun setup(surveyFrame: SurveyFrame, textQuestion: TextQuestion) {
        this.surveyFrame = surveyFrame
        this.question = textQuestion
        txtTitle.text = question.text.get()
        txtInstruction.text = question.instruction.get()
    }
}