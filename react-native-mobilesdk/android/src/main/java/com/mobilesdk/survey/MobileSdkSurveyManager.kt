package com.mobilesdk.survey

class MobileSdkSurveyManager {
    companion object {
        private val activeSurvey: MutableMap<String, MobileSdkSurveyWrapper> = mutableMapOf()
    }

    fun getSurvey(serverId: String, programKey: String, surveyId: String): MobileSdkSurveyWrapper? {
        return activeSurvey[getKey(serverId, programKey, surveyId)]
    }

    fun addSurvey(serverId: String, programKey: String, surveyId: String, survey: MobileSdkSurveyWrapper) {
        activeSurvey[getKey(serverId, programKey, surveyId)] = survey
    }

    fun removeSurvey(serverId: String, programKey: String, surveyId: String) {
        activeSurvey.remove(getKey(serverId, programKey, surveyId))
    }

    private fun getKey(serverId: String, programKey: String, surveyId: String): String {
        return "${serverId}_${programKey}_$surveyId"
    }
}