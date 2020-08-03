package com.confirmit.testsdkapp.domain

import androidx.appcompat.app.AlertDialog
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.trigger.ProgramCallback
import com.confirmit.mobilesdk.trigger.TriggerInfo
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.web.SurveyWebViewFragment
import com.confirmit.testsdkapp.MainApplication
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.utils.Utils
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

class TriggerCallback : ProgramCallback {
    override fun onSurveyDownloadCompleted(triggerInfo: TriggerInfo, surveyId: String, exception: Exception?) {
        if (exception != null) {
            val context = ConfirmitSDK.androidContext as MainApplication
            Utils.showError(context.activity!!, "Survey $surveyId download failed", exception.localizedMessage)
        }
    }

    override fun onSurveyStart(config: SurveyFrameConfig) {
        if (OverlayService.isRunning) {
            OverlaySurveyManager.startSurvey(config)
        } else {
            val surveyManager = SurveyManager()
            val context = ConfirmitSDK.androidContext as MainApplication
            surveyManager.launchSurvey(context.activity!!, SurveyModel(config.serverId, config.surveyId), config)
        }
    }

    override fun onScenarioStart(triggerInfo: TriggerInfo, exception: Exception?) {
        exception?.let {
            val context = ConfirmitSDK.androidContext as MainApplication
            Utils.showError(context.activity!!, "Scenario Failed to Start", exception.localizedMessage)
        }
    }

    override fun onScenarioError(triggerInfo: TriggerInfo, exception: Exception) {
        val context = ConfirmitSDK.androidContext as MainApplication
        Utils.showError(context.activity!!, "Scenario Failed", exception.localizedMessage)
    }

    override fun onAppFeedback(triggerInfo: TriggerInfo, data: Map<String, String?>) {
        val context = ConfirmitSDK.androidContext as MainApplication
        context.activity?.let {
            it.runOnUiThread {
                AlertDialog.Builder(it)
                        .setTitle("Write a review!")
                        .setMessage("Would you like to write a review")
                        .setPositiveButton("Write a review!") { _, _ ->
                        }
                        .setNegativeButton("Remind Me Later") { _, _ ->
                        }
                        .show()
            }
        }
    }

    override fun onWebSurveyStart(surveyWebView: SurveyWebViewFragment) {
        val context = ConfirmitSDK.androidContext as MainApplication
        context.activity?.let {
            (it as MainActivity).fetcherListener?.endIdle()
            it.runOnUiThread {
                surveyWebView.setCallback(WebViewCallback())
                surveyWebView.show(it.supportFragmentManager, "SurveyWebViewFragment")
            }
        }
    }
}