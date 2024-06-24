package com.mobilesdk.survey

import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.SurveyFrameActionResult
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.confirmit.mobilesdk.ui.SurveyPage
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import okhttp3.internal.wait

class MobileSdkSurveyWrapper(private val reactContext: ReactContext, private val config: SurveyFrameConfig) : SurveyFrameLifecycleListener {
    private val manager = MobileSdkSurveyManager()
    var surveyFrame: SurveyFrame? = SurveyFrame()

    fun startSurvey(customData: Map<String, String?>, respondentValue: Map<String, String>): SurveyFrameActionResult? {
        for (data in customData) {
            config.customData[data.key] = data.value
        }
        config.respondentValues = respondentValue
        surveyFrame?.surveyFrameLifeCycleListener = this
        surveyFrame?.load(config)

        return surveyFrame?.start()
    }

    override fun onSurveyErrored(page: SurveyPage, values: Map<String, String?>, exception: Exception) {
        val results = Arguments.createMap()
        for (value in values) {
            results.putString(value.key, value.value)
        }

        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnSurveyErrored", Arguments.createMap().apply {
                putString("forwardText", page.forwardText)
                putString("backwardText", page.backwardText)
                putString("okText", page.okText)
                putBoolean("showForward", page.showForward)
                putBoolean("showBackward", page.showBackward)
                putMap("error", Arguments.createMap().apply {
                    putString("message", exception.localizedMessage)
                    putString("stack", exception.stackTraceToString())
                })
                putMap("values", results)
            })
    }

    override fun onSurveyFinished(page: SurveyPage, values: Map<String, String?>) {
        val results = Arguments.createMap()
        for (value in values) {
            results.putString(value.key, value.value)
        }

        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnSurveyFinished", Arguments.createMap().apply {
                putString("forwardText", page.forwardText)
                putString("backwardText", page.backwardText)
                putString("okText", page.okText)
                putBoolean("showForward", page.showForward)
                putBoolean("showBackward", page.showBackward)
                putMap("values", results)
            })

        cleanup()
    }

    override fun onSurveyPageReady(page: SurveyPage) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnSurveyPageReady", Arguments.createMap().apply {
                putString("serverId", config.serverId)
                putString("programKey", config.programKey)
                putString("surveyId", config.surveyId)
                putString("forwardText", page.forwardText)
                putString("backwardText", page.backwardText)
                putString("okText", page.okText)
                putBoolean("showForward", page.showForward)
                putBoolean("showBackward", page.showBackward)
            })
    }

    override fun onSurveyQuit(values: Map<String, String?>) {
        val results = Arguments.createMap()
        for (value in values) {
            results.putString(value.key, value.value)
        }
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("__mobileOnSurveyQuit", Arguments.createMap().apply {
                putMap("values", results)
            })

        cleanup()
    }

    private fun cleanup() {
        surveyFrame?.surveyFrameLifeCycleListener = null
        surveyFrame = null
        manager.removeSurvey(config.serverId, config.programKey ?: "", config.surveyId)
    }
}