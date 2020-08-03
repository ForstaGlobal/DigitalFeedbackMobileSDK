package com.confirmit.testsdkapp.domain

import android.content.Context
import android.content.ContextWrapper
import android.graphics.PixelFormat
import android.view.WindowManager
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.database.externals.Program
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.layouts.defualtlayoutview.DefaultSurveyView
import com.confirmit.testsdkapp.layouts.defualtlayoutview.DefaultSurveyViewCallback
import com.confirmit.testsdkapp.ui.UiHandler
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

object OverlaySurveyManager: DefaultSurveyViewCallback {
    private var windowManager: WindowManager = ContextWrapper(ConfirmitSDK.androidContext).getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var surveyView: DefaultSurveyView? = null

    fun startSurvey(config: SurveyFrameConfig) {
        val handler = UiHandler()
        handler.createHandler()
        handler.postUi {
            val surveyManager = SurveyManager()

            surveyView = surveyManager.getSurveyView(SurveyModel(config.serverId, config.surveyId), config) as DefaultSurveyView
            surveyView!!.listener = this

            val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    OverlayService.LAYOUT_FLAG,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                    PixelFormat.TRANSLUCENT)

            windowManager.addView(surveyView, params)
        }
    }

    fun getProgram(): Program? {
        return TriggerManager().getPrograms().firstOrNull { it.programKey == AppConfigs.enabledOverlayProgram }
    }

    private fun endSurvey() {
        windowManager.removeView(surveyView)
    }

    override fun onSurveyClosed() {
        endSurvey()
    }
}