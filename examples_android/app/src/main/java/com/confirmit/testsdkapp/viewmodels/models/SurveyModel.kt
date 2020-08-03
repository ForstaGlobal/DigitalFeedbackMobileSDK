package com.confirmit.testsdkapp.viewmodels.models

import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.database.externals.Survey
import com.confirmit.mobilesdk.database.externals.SurveyLanguage
import com.confirmit.testsdkapp.AppExceptions
import com.confirmit.testsdkapp.SurveyConfigs
import java.io.Serializable

class SurveyModel : Serializable {
    var survey: Survey
        private set
    var selectedLanguage: SurveyLanguage
    var configs: SurveyConfigs
        private set

    constructor(serverId: String, surveyId: String) {
        val survey = SurveySDK.getSurvey(serverId, surveyId)
                ?: throw AppExceptions("Survey is not available")
        this.survey = survey
        this.selectedLanguage = survey.getDefaultLanguage()
        this.configs = SurveyConfigs(survey.serverId, survey.surveyId)
    }

    constructor(survey: Survey) {
        this.survey = survey
        this.selectedLanguage = survey.getDefaultLanguage()
        this.configs = SurveyConfigs(survey.serverId, survey.surveyId)
    }

    fun update(survey: Survey) {
        this.survey = survey
    }

    fun getRespondentValues(): Map<String, String> {
        val values = mutableMapOf<String, String>()
        val raw = configs.respondentValue
        raw.split(";").forEach {
            val keyValue = it.split("=")
            if (keyValue.size == 2) {
                val key = keyValue[0]
                val value = keyValue[1]
                values[key] = value
            }
        }
        return values
    }

}