package com.mobilesdk.module

import com.confirmit.mobilesdk.trigger.ProgramCallback
import com.confirmit.mobilesdk.trigger.TriggerInfo
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.web.SurveyWebViewFragment
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.mobilesdk.survey.MobileSdkSurveyManager
import com.mobilesdk.survey.MobileSdkSurveyWrapper

class TriggerCallback(
    private val reactContext: ReactContext,
    private val serverId: String,
    private val programKey: String
) : ProgramCallback {
    override fun onAppFeedback(triggerInfo: TriggerInfo, data: Map<String, String?>) {}

    override fun onScenarioError(triggerInfo: TriggerInfo, exception: Exception) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnScenarioError", Arguments.createMap().apply {
                putString("serverId", serverId)
                putString("programKey", programKey)
                putString("error", exception.localizedMessage)
            })
    }

    override fun onScenarioLoad(triggerInfo: TriggerInfo, exception: Exception?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnScenarioLoad", Arguments.createMap().apply {
                putString("serverId", serverId)
                putString("programKey", programKey)
                putString("error", exception?.localizedMessage)
            })
    }

    override fun onSurveyDownloadCompleted(
        triggerInfo: TriggerInfo,
        surveyId: String,
        exception: Exception?
    ) {
    }

    override fun onSurveyStart(config: SurveyFrameConfig) {
        MobileSdkSurveyManager().addSurvey(config.serverId, programKey, config.surveyId, MobileSdkSurveyWrapper(reactContext, config))

        val values = Arguments.createMap().apply {
            putString("serverId", serverId)
            putString("programKey", programKey)
            putString("surveyId", config.surveyId)
        }

        config.languageId?.let {
            values.putInt("languageId", it)
        }

        val customData = Arguments.createMap()
        for (data in config.customData) {
            customData.putString(data.key, data.value)
        }
        values.putMap("customData", customData)

        val respondentValue = Arguments.createMap()
        for (respondent in config.respondentValues) {
            respondentValue.putString(respondent.key, respondent.value)
        }
        values.putMap("respondentValue", respondentValue)

        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnSurveyStart", values)
    }

    override fun onWebSurveyStart(fragment: SurveyWebViewFragment) {
        val result = fragment.getSurveyUrl()
        val token = result.token
        val url = result.url
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnWebSurveyStart", Arguments.createMap().apply {
                putString("serverId", serverId)
                putString("programKey", programKey)
                putString("token", token)
                putString("url", url)
            })
    }
}
