package com.example.myapp.domain

import androidx.fragment.app.FragmentActivity
import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.example.myapp.MainActivity
import com.example.myapp.fragments.SurveyLayoutOnCloseListener
import com.example.myapp.fragments.viewmodels.models.SurveyModel
import com.example.myapp.layouts.defaultlayout.DefaultSurveyFragment
import com.example.myapp.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SurveyManager {
    fun displaySurvey(activity: FragmentActivity, survey: SurveyModel, config: SurveyFrameConfig? = null) {
        val surveyFragment = DefaultSurveyFragment()
        surveyFragment.setOnCloseListener(object : SurveyLayoutOnCloseListener {
            override fun onCloseError(error: String) {
                Utils.showError(activity, "Survey Error", error)
            }
        })
        surveyFragment.initialize(survey, config)
        surveyFragment.show(activity.supportFragmentManager, "survey_dialog_fragment")
    }
}