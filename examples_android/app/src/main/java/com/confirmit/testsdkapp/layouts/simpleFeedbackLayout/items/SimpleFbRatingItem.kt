package com.confirmit.testsdkapp.layouts.simplefeedbacklayout.items

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.SingleQuestion
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.layouts.simplefeedbacklayout.SimpleFbItem

enum class SimpleFbFaceIcon(val id: Int) {
    WORST(R.drawable.ic_sentiment_very_dissatisfied),
    VERY_BAD(R.drawable.ic_mood_bad),
    BAD(R.drawable.ic_sentiment_dissatisfied),
    GOOD(R.drawable.ic_sentiment_satisfied),
    VERY_GOOD(R.drawable.ic_mood),
    BEST(R.drawable.ic_sentiment_very_satisfied)
}

enum class SimpleFbStarIcon(val id: Int) {
    ON(R.drawable.ic_round_star),
    OFF(R.drawable.ic_round_fiber_manual_record)
}

interface SimpleFbRatingItemListener {
    fun onNextClicked(question: SingleQuestion): List<ValidationQuestionError>
    fun onFaceChanged(face: SimpleFbFaceIcon)
}

class SimpleFbRatingItem(private val view: View) : SimpleFbItem {

    private val btnNext: Button = view.findViewById(R.id.btnNext)
    private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
    private val txtInst: TextView = view.findViewById(R.id.txtInstruction)
    private val txtValidation: TextView = view.findViewById(R.id.txtValidation)
    private val ctnRanking: LinearLayout = view.findViewById(R.id.containerRanking)
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

    private lateinit var surveyFrame: SurveyFrame
    private lateinit var question: SingleQuestion

    var listener: SimpleFbRatingItemListener? = null

    private var initialSelected: Int = -1
    private var previouslySelected: Int = -1

    init {
        txtValidation.text = ""

        btnNext.setOnClickListener {
            listener?.onNextClicked(question)?.let { validationErrors ->
                if (validationErrors.isNotEmpty()) {
                    txtValidation.text = "Wait! We haven't heard from you yet!"
                }
            }
        }
    }

    override fun getView(): View {
        return view
    }

    fun setup(surveyFrame: SurveyFrame, singleQuestion: SingleQuestion) {
        this.surveyFrame = surveyFrame
        this.question = singleQuestion

        txtTitle.text = question.text.get()
        txtInst.text = question.instruction.get()

        question.selected()?.let { selected ->
            initialSelected = selected.answers.indexOfFirst { it.code == selected.code }
        }

        imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, R.color.font))
        imgIcon.setImageResource(SimpleFbFaceIcon.GOOD.id)

        createStars(imgIcon.context, question.answers.size)
    }

    private fun updateFace(index: Int, total: Int) {
        val ratio: Float = index.toFloat() / total.toFloat()
        val icon: SimpleFbFaceIcon = when (total) {
            0, 1 -> {
                SimpleFbFaceIcon.GOOD
            }
            2 -> {
                if (index == 0) SimpleFbFaceIcon.BAD else SimpleFbFaceIcon.GOOD
            }
            3, 4, 5 -> {
                when {
                    ratio < 0.25 -> SimpleFbFaceIcon.VERY_BAD
                    ratio < 0.5 -> SimpleFbFaceIcon.BAD
                    ratio < 0.75 -> SimpleFbFaceIcon.GOOD
                    else -> SimpleFbFaceIcon.VERY_GOOD
                }
            }
            else -> {
                when {
                    ratio < 0.166 -> SimpleFbFaceIcon.WORST
                    ratio < 0.333 -> SimpleFbFaceIcon.VERY_BAD
                    ratio < 0.5 -> SimpleFbFaceIcon.BAD
                    ratio < 0.666 -> SimpleFbFaceIcon.GOOD
                    ratio < 0.833 -> SimpleFbFaceIcon.VERY_GOOD
                    else -> SimpleFbFaceIcon.BEST
                }
            }
        }

        val oa1 = ObjectAnimator.ofFloat(imgIcon, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(imgIcon, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                imgIcon.setImageResource(icon.id)
                oa2.duration = 100
                oa2.start()
            }
        })
        oa1.duration = 100
        oa1.start()

        listener?.onFaceChanged(icon)
    }

    private fun updateStars(selected: Int) {
        val start: Int
        val end: Int

        when {
            previouslySelected == -1 -> {
                start = 0
                end = selected
            }
            selected == previouslySelected -> {
                start = previouslySelected
                end = previouslySelected
            }
            selected == previouslySelected + 1 -> {
                start = selected
                end = selected
            }
            selected > previouslySelected -> {
                start = previouslySelected + 1
                end = selected
            }
            else -> {
                start = selected
                end = previouslySelected
            }
        }

        for (i in start..end) {
            var icon: SimpleFbStarIcon = SimpleFbStarIcon.OFF
            if (i <= selected) {
                icon = SimpleFbStarIcon.ON
            }
            (ctnRanking.getChildAt(i) as? LinearLayout)?.let {

                val image = it.getChildAt(0) as ImageView
                when (icon) {
                    SimpleFbStarIcon.ON -> {
                        val anim = AlphaAnimation(1.0f, 0.0f)
                        anim.duration = 200
                        anim.repeatCount = 1
                        anim.repeatMode = Animation.REVERSE
                        anim.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationEnd(animation: Animation?) {}
                            override fun onAnimationStart(animation: Animation?) {}
                            override fun onAnimationRepeat(animation: Animation?) {
                                image.setImageResource(icon.id)
                            }
                        })

                        image.startAnimation(anim)
                    }
                    SimpleFbStarIcon.OFF -> {
                        image.setImageResource(icon.id)
                    }
                }
            }
        }
        previouslySelected = selected
    }

    private fun createStars(context: Context, totalCount: Int) {
        for (i in 0 until totalCount) {
            val density = context.resources.displayMetrics.density

            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = LinearLayout.LayoutParams((density * 48).toInt(), (density * 48).toInt())
            linearLayout.gravity = Gravity.CENTER

            linearLayout.setOnClickListener {
                updateStars(i)
                question.select(question.answers[i])
                updateFace(i, question.answers.size)
            }

            val image = ImageView(context)
            image.setImageResource(SimpleFbStarIcon.OFF.id)
            image.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            image.setColorFilter(ContextCompat.getColor(context, R.color.simpleFbSurveyFont))
            image.tag = i

            linearLayout.addView(image)
            ctnRanking.addView(linearLayout)
        }
    }
}