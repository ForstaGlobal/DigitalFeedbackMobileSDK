package com.example.myapp.fragments.viewmodels.models

import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.database.externals.Survey
import com.confirmit.mobilesdk.database.externals.SurveyLanguage
import java.io.Serializable

class SurveyModel : Serializable {
    var survey: Survey
        private set
    var selectedLanguage: SurveyLanguage

    constructor(serverId: String, surveyId: String) {
        val survey = SurveySDK.getSurvey(serverId, surveyId)
            ?: throw Exception("Survey is not available")
        this.survey = survey
        this.selectedLanguage = survey.getDefaultLanguage()
    }

    constructor(survey: Survey) {
        this.survey = survey
        this.selectedLanguage = survey.getDefaultLanguage()
    }

    fun update(survey: Survey) {
        this.survey = survey
    }
}