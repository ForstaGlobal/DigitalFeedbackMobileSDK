package com.confirmit.testsdkapp.viewmodels

import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

class OverviewViewModel(serverId: String, surveyId: String) {
    var survey: SurveyModel = SurveyModel(serverId, surveyId)
        private set

    fun updateSelectedLanguage(langId: Int) {
        survey.survey.languages.first { it.id == langId }.let {
            survey.selectedLanguage = it
        }
    }

    fun removeSurvey(): Boolean {
        return try {
            SurveySDK.deleteSurvey(survey.survey.serverId, survey.survey.surveyId)
            true
        } catch (_: Exception) {
            false
        }
    }
}