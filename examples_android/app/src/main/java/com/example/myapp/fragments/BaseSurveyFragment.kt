package com.example.myapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.example.myapp.R
import com.example.myapp.fragments.viewmodels.models.SurveyModel
import com.example.myapp.utils.UiHandler

interface SurveyLayoutOnCloseListener {
    fun onCloseError(error: String)
}

abstract class BaseSurveyFragment : DialogFragment() {
    private val surveyFrame = SurveyFrame()
    protected lateinit var surveyModel: SurveyModel
    protected var surveyFrameConfig: SurveyFrameConfig? = null

    private var onCloseListener: SurveyLayoutOnCloseListener? = null

    private lateinit var rootView: View

    protected abstract var layoutId: Int

    private val uiHandler: UiHandler = UiHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        uiHandler.createHandler()
        rootView = inflater.inflate(layoutId, container, false)
        initContentView(rootView, inflater, container, savedInstanceState)

        return rootView
    }

    override fun onDestroyView() {
        uiHandler.releaseHandler()
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    abstract fun initContentView(rootView: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
    }

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
        uiHandler.postUi {
            dismiss()
            onCloseListener?.onCloseError(message)
        }
    }

    fun errorCloseSurvey(e: Exception) {
        uiHandler.postUi {
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