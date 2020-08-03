package com.confirmit.testsdkapp.layouts.simplefeedbacklayout

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.confirmit.mobilesdk.ui.SurveyPage
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.SingleQuestion
import com.confirmit.mobilesdk.ui.questions.TextQuestion
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.fragments.BaseSurveyFragment
import com.confirmit.testsdkapp.layouts.simplefeedbacklayout.items.*
import com.confirmit.testsdkapp.utils.darker
import com.confirmit.testsdkapp.utils.lighter

interface SimpleFbItem {
    fun getView(): View
}

class SimpleFbFragment : BaseSurveyFragment(), SurveyFrameLifecycleListener, SimpleFbRatingItemListener, SimpleFbTextItemListener {
    override var layoutId: Int = R.layout.fragment_simple_feedback_layout

    private lateinit var surveyLayout: ConstraintLayout
    private lateinit var viewPager: ViewPager
    private lateinit var btnClose: ImageButton
    private lateinit var ratingItem: SimpleFbRatingItem
    private lateinit var textItem: SimpleFbOpenTextItem

    private var isInitialized: Boolean = false
    private val gradients: MutableMap<SimpleFbFaceIcon, GradientDrawable> = mutableMapOf()

    private lateinit var previousBackground: GradientDrawable

    override fun initContentView(rootView: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
        surveyLayout = rootView.findViewById(R.id.surveyLayout)
        viewPager = rootView.findViewById(R.id.viewPager)
        btnClose = rootView.findViewById(R.id.btnClose)

        ratingItem = SimpleFbRatingItem(inflater.inflate(R.layout.simple_feedback_rating, null, false))
        ratingItem.listener = this

        textItem = SimpleFbOpenTextItem(inflater.inflate(R.layout.simple_feedback_open_text, null, false))
        textItem.listener = this

        btnClose.setOnClickListener {
            surveyFrame.quit(AppConfigs.uploadIncomplete)
        }

        previousBackground = createGradientList(requireContext())
        surveyLayout.background = previousBackground

        loadFrame(this)
    }

    override fun onSurveyPageReady(page: SurveyPage) {
        val errorMessage = "Survey must consist of 2 questions with [Single, Text] combination."

        if (isInitialized) {
            errorCloseSurvey(errorMessage)
            return
        }

        val question = page.questions
        if (question.size != 2) {
            errorCloseSurvey(errorMessage)
            return
        }

        val singleQuestion = question[0] as? SingleQuestion
        if (singleQuestion == null) {
            errorCloseSurvey(errorMessage)
            return
        }

        val textQuestion = question[1] as? TextQuestion
        if (textQuestion == null) {
            errorCloseSurvey(errorMessage)
            return
        }

        ratingItem.setup(surveyFrame, singleQuestion)
        textItem.setup(surveyFrame, textQuestion)
        viewPager.adapter = SimpleFbAdapter(listOf(ratingItem, textItem))
    }

    override fun onNextClicked(question: SingleQuestion): List<ValidationQuestionError> {
        return try {
            val result = question.validate()
            if (result.isEmpty()) {
                viewPager.currentItem = 1
            }
            result
        } catch (e: Exception) {
            errorCloseSurvey(e)
            mutableListOf()
        }
    }

    override fun onFaceChanged(face: SimpleFbFaceIcon) {
        updateColor(face)
    }

    override fun onSubmitClicked(question: TextQuestion): List<ValidationQuestionError> {
        return try {
            val result = question.validate()
            if (result.isEmpty()) {
                surveyFrame.next()
            }
            result
        } catch (e: Exception) {
            errorCloseSurvey(e)
            mutableListOf()
        }
    }

    override fun onSurveyErrored(page: SurveyPage, values: Map<String, String?>, exception: Exception) {
        errorCloseSurvey(exception)
    }

    override fun onSurveyFinished(page: SurveyPage, values: Map<String, String?>) {
        closeSurvey()
    }

    override fun onSurveyQuit(values: Map<String, String?>) {
        closeSurvey()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return dialog
    }

    private fun createGradientList(context: Context): GradientDrawable {
        gradients.clear()

        val from = ContextCompat.getColor(context, R.color.simpleFbBackgroundFrom).toLong()
        val to = ContextCompat.getColor(context, R.color.simpleFbBackgroundTo).toLong()
        gradients[SimpleFbFaceIcon.WORST] = createGradient(from.darker(15f), to.darker(15f))
        gradients[SimpleFbFaceIcon.VERY_BAD] = createGradient(from.darker(10f), to.darker(10f))
        gradients[SimpleFbFaceIcon.BAD] = createGradient(from.darker(5f), to.darker(5f))
        val default = createGradient(from, to)
        gradients[SimpleFbFaceIcon.GOOD] = default
        gradients[SimpleFbFaceIcon.VERY_GOOD] = createGradient(from.lighter(5f), to.lighter(5f))
        gradients[SimpleFbFaceIcon.BEST] = createGradient(from.lighter(10f), to.lighter(10f))

        return default
    }

    private fun createGradient(from: Long, to: Long): GradientDrawable {
        val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(from.toInt(), to.toInt()))
        gd.cornerRadius = 0f

        return gd
    }

    private fun updateColor(face: SimpleFbFaceIcon) {
        val currentBackground = gradients[face]

        val color = arrayOf(previousBackground, currentBackground)
        val trans = TransitionDrawable(color)
        surveyLayout.background = trans
        trans.startTransition(500)

        previousBackground = currentBackground!!
    }
}