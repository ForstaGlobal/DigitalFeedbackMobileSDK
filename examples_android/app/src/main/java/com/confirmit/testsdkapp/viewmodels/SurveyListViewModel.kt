package com.confirmit.testsdkapp.viewmodels

import android.util.Log
import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.database.externals.Server
import com.confirmit.mobilesdk.database.externals.Survey
import com.confirmit.testsdkapp.utils.ServerManager
import com.confirmit.testsdkapp.utils.ServerPref
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.Serializable

class SurveyListViewModel : Serializable {
    var surveys: MutableList<SurveyModel> = mutableListOf()

    init {
        try {
            SurveySDK.getSurveyList().forEach {
                surveys.add(SurveyModel(it))
            }
        } catch (_: Exception) {
        }
    }

    fun reloadSurveys() {
        try {
            surveys.clear()
            SurveySDK.getSurveyList().forEach {
                surveys.add(SurveyModel(it))
            }
        } catch (_: Exception) {
        }
    }

    fun downloadAllAsync(completion: (Boolean) -> Unit) = GlobalScope.async {
        val results = surveys.map {
            return@map Pair(it, SurveySDK.downloadSurveyAsync(it.survey.serverId, it.survey.surveyId))
        }

        val updatedSurveys = results.mapNotNull {
            try {
                val survey = it.first
                it.second.await()
                return@mapNotNull SurveySDK.getSurvey(survey.survey.serverId, survey.survey.surveyId)!!
            } catch (e: Exception) {
                return@mapNotNull null
            }
        }

        processSurveys(updatedSurveys, completion)
    }

    fun downloadAsync(serverId: String, surveyId: String, completion: (Boolean) -> Unit) = GlobalScope.async {
        try {
            val result = SurveySDK.downloadSurveyAsync(serverId, surveyId).await()
            if (result.result) {
                val updated = SurveySDK.getSurvey(serverId, surveyId)!!
                processSurveys(mutableListOf(updated), completion)
            } else {
                throw result.exception!!
            }

        } catch (e: Exception) {
            Log.e("MobileSDK", "Failed to downloadAsync survey", e)
            completion(false)
        }
    }

    fun setNewServer(serverId: String, clientId: String, clientSecret: String): Server {
        val server =  ServerManager.setNewConfiguration(serverId, clientId, clientSecret)
        ServerManager.writeServerToPref(serverId, ServerPref(clientId, clientSecret))
        return server
    }

    private fun processSurveys(updatedSurvey: List<Survey>, completion: (Boolean) -> Unit) {
        updatedSurvey.forEach { survey ->
            val existIndex = surveys.indexOfFirst { survey.serverId == it.survey.serverId && survey.surveyId == it.survey.surveyId }
            if (existIndex >= 0) {
                surveys[existIndex].update(survey)
            } else {
                try {
                    surveys.add(SurveyModel(survey))
                } catch (_: Exception) {
                }
            }
        }

        completion(updatedSurvey.isNotEmpty())
    }
}