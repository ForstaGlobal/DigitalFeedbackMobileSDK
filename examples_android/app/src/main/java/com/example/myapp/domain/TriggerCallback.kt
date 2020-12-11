package com.example.myapp.domain

import androidx.appcompat.app.AlertDialog
import com.confirmit.mobilesdk.trigger.ProgramCallback
import com.confirmit.mobilesdk.trigger.TriggerInfo
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.web.SurveyWebViewFragment
import com.example.myapp.MainActivity
import com.example.myapp.MainApplication
import com.example.myapp.R
import com.example.myapp.fragments.viewmodels.models.SurveyModel
import com.example.myapp.utils.Utils

// NOTE: For quick implementation, we reference activity directly. In general, activity shouldn't be referenced directly
class TriggerCallback : ProgramCallback {
    override fun onSurveyDownloadCompleted(triggerInfo: TriggerInfo, surveyId: String, exception: Exception?) {
        if (exception != null) {
            val activity = MainApplication.instance!!.activity
            Utils.showError(activity!!, "Survey $surveyId download failed", exception.localizedMessage)
        }
    }

    override fun onSurveyStart(config: SurveyFrameConfig) {
        val surveyManager = SurveyManager()
        val activity = MainApplication.instance!!.activity
        surveyManager.launchSurvey(activity!!, SurveyModel(config.serverId, config.surveyId), config)
    }

    override fun onWebSurveyStart(fragment: SurveyWebViewFragment) {
        val activity = MainApplication.instance!!.activity as MainActivity
        activity.runOnUiThread {
            fragment.setCallback(WebViewCallback())
            fragment.show(activity.supportFragmentManager, "SurveyWebViewFragment")
        }
    }

    override fun onScenarioError(triggerInfo: TriggerInfo, exception: Exception) {
        val activity = MainApplication.instance!!.activity
        Utils.showError(activity!!, "Scenario Failed", exception.localizedMessage)
    }

    override fun onScenarioLoad(triggerInfo: TriggerInfo, exception: Exception?) {
        exception?.let {
            val activity = MainApplication.instance!!.activity
            Utils.showError(activity!!, "Scenario Failed to Start", exception.localizedMessage)
        }
    }

    override fun onAppFeedback(triggerInfo: TriggerInfo, data: Map<String, String?>) {
        val activity = MainApplication.instance!!.activity
        activity?.runOnUiThread {
            AlertDialog.Builder(activity)
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