package com.confirmit.testsdkapp.domain

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.SurveyLayout
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.fragments.BaseSurveyFragment
import com.confirmit.testsdkapp.fragments.SurveyLayoutOnCloseListener
import com.confirmit.testsdkapp.layouts.defaultlayout.DefaultSurveyFragment
import com.confirmit.testsdkapp.layouts.defualtlayoutview.DefaultSurveyView
import com.confirmit.testsdkapp.layouts.simplecardlayout.SimpleCardFragment
import com.confirmit.testsdkapp.layouts.simplefeedbacklayout.SimpleFbFragment
import com.confirmit.testsdkapp.utils.Utils
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SurveyManager {
    fun launchSurvey(activity: FragmentActivity, survey: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = null) {
        if (AppConfigs.onDemandDownload) {
            (activity as MainActivity).showProgress()
            GlobalScope.launch {
                val result = SurveySDK.downloadSurveyAsync(survey.survey.serverId, survey.survey.surveyId).await()
                if (result.result) {
                    displaySurvey(activity, survey, surveyFrameConfig)
                } else {
                    Utils.showError(activity, "Survey Error", "Failed to update survey")
                }
                activity.hideProgress()
            }

        } else {
            displaySurvey(activity, survey, surveyFrameConfig)
        }
    }

    fun getSurveyView(survey: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = null): View {
        val surveyView = DefaultSurveyView(ConfirmitSDK.androidContext)
        // return null for special handling of Java layout
        surveyView.initialize(survey, surveyFrameConfig)
        return surveyView
    }

    private fun displaySurvey(activity: FragmentActivity, survey: SurveyModel, config: SurveyFrameConfig? = null) {
        val surveyFragment: BaseSurveyFragment? = when (survey.configs.surveyLayout) {
            SurveyLayout.DEFAULT -> {
                DefaultSurveyFragment()
            }
            SurveyLayout.CARD -> {
                SimpleCardFragment()
            }
            SurveyLayout.SIMPLE_FEEDBACK -> {
                SimpleFbFragment()
            }
            SurveyLayout.DEFAULT_JAVA -> null // return null for special handling of Java layout
        }
        val f = activity.supportFragmentManager

        if (surveyFragment != null) {
            surveyFragment.setOnCloseListener(object : SurveyLayoutOnCloseListener {
                override fun onCloseError(error: String) {
                    Utils.showError(activity, "Survey Error", error)
                }
            })
            surveyFragment.initialize(survey, config)
            surveyFragment.show(f, "survey_dialog_fragment")
        } else {
            // since Java implementation doesn't inherit from BaseSurveyFragment, we added special handling to display this dialog
            val javaSurveyFragment = com.confirmit.testsdkapp.layouts.defaultlayoutjava.DefaultSurveyFragment()
            javaSurveyFragment.setOnCloseListener(JavaCloseListener(activity))
            javaSurveyFragment.initialize(survey)
            javaSurveyFragment.show(f, "java_survey_dialog_fragment")
        }
    }

    // app crashes if we used lambda
    class JavaCloseListener(private val activity: Activity): com.confirmit.testsdkapp.layouts.defaultlayoutjava.SurveyLayoutOnCloseListener {
        override fun onCloseError(error: String) {
            Utils.showError(activity, "Survey Error", error)
        }
    }
}