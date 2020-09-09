package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

interface SurveyLayoutOnCloseListener {
    fun onCloseError(error: String)
}

abstract class BaseSurveyFragment : BaseDialogFragment() {
    protected val surveyFrame = SurveyFrame()
    protected lateinit var surveyModel: SurveyModel
    protected var surveyFrameConfig: SurveyFrameConfig? = null

    private var onCloseListener: SurveyLayoutOnCloseListener? = null

    fun initialize(surveyModel: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = null) {
        this.surveyModel = surveyModel
        this.surveyFrameConfig = surveyFrameConfig
    }

    fun loadFrame(surveyFrameLifecycleListener: SurveyFrameLifecycleListener) {
        try {
            if (surveyFrameConfig == null) {
                surveyFrameConfig = SurveyFrameConfig(surveyModel.survey.serverId, surveyModel.survey.surveyId)
            }

            surveyFrameConfig!!.languageId = surveyModel.selectedLanguage.id
            surveyFrameConfig!!.respondentValues = surveyModel.getRespondentValues()

            surveyFrame.load(surveyFrameConfig!!)
            surveyFrame.surveyFrameLifeCycleListener = surveyFrameLifecycleListener
            surveyFrame.start()
        } catch (e: Exception) {
            errorCloseSurvey(e)
        }
    }

    fun closeSurvey() {
        hideKeyboard()
        dismiss()
    }

    fun errorCloseSurvey(message: String) {
        postUi {
            dismiss()
            onCloseListener?.onCloseError(message)
        }
    }

    fun errorCloseSurvey(e: Exception) {
        postUi {
            dismiss()
            onCloseListener?.onCloseError("$e")
        }
    }

    fun setOnCloseListener(listener: SurveyLayoutOnCloseListener) {
        onCloseListener = listener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.dialog_fade
    }
}