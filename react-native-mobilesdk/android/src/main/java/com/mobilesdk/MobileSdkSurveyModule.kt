package com.mobilesdk

import android.app.Application
import com.confirmit.mobilesdk.ui.QuestionType
import com.confirmit.mobilesdk.ui.SurveyFrameActionResult
import com.confirmit.mobilesdk.ui.questions.DefaultQuestion
import com.confirmit.mobilesdk.ui.questions.InfoQuestion
import com.confirmit.mobilesdk.ui.questions.MultiQuestion
import com.confirmit.mobilesdk.ui.questions.NumericQuestion
import com.confirmit.mobilesdk.ui.questions.QuestionAnswer
import com.confirmit.mobilesdk.ui.questions.SingleQuestion
import com.confirmit.mobilesdk.ui.questions.TextQuestion
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableArray
import com.mobilesdk.survey.MobileSdkSurveyManager
import com.mobilesdk.survey.MobileSdkSurveyWrapper

class MobileSdkSurveyModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "MobileSdkSurvey"
    }

    private val surveyManager = MobileSdkSurveyManager()

    override fun getName(): String {
        return NAME
    }

    // Survey Page Methods
    @ReactMethod
    fun getTitle(serverId: String, programKey: String, surveyId: String, promise: Promise) {
        promise.resolve(surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.title?.get())
    }

    @ReactMethod
    fun getText(serverId: String, programKey: String, surveyId: String, promise: Promise) {
        promise.resolve(surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.text?.get())
    }

    @ReactMethod
    fun getQuestions(serverId: String, programKey: String, surveyId: String, promise: Promise) {
        val results = Arguments.createArray()
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.let { questions ->
            for (question in questions) {
                val result = Arguments.createMap().apply {
                    putString("id", question.id)
                    putString("nodeType", question.nodeType.toString())
                }

                val errors = Arguments.createArray()
                when (question) {
                    is InfoQuestion -> {
                        result.putString("title", question.title.get())
                        result.putString("text", question.text.get())
                        result.putString("instruction", question.instruction.get())
                    }

                    is TextQuestion -> {
                        result.putString("title", question.title.get())
                        result.putString("text", question.text.get())
                        result.putString("instruction", question.instruction.get())
                    }

                    is NumericQuestion -> {
                        result.putString("title", question.title.get())
                        result.putString("text", question.text.get())
                        result.putString("instruction", question.instruction.get())
                    }

                    is SingleQuestion -> {
                        result.putString("title", question.title.get())
                        result.putString("text", question.text.get())
                        result.putString("instruction", question.instruction.get())
                        result.putInt("appearance", question.appearance.value)
                        result.putArray("answers", getAnswers(question.answers))
                    }

                    is MultiQuestion -> {
                        result.putString("title", question.title.get())
                        result.putString("text", question.text.get())
                        result.putString("instruction", question.instruction.get())
                        result.putInt("appearance", question.appearance.value)
                        result.putArray("answers", getAnswers(question.answers))
                    }
                }

                if (question is DefaultQuestion) {
                    for (error in question.errors) {
                        errors.pushMap(Arguments.createMap().apply {
                            putString("code", error.code.name)
                            putString("message", error.message)
                        })
                    }
                }

                result.putArray("errors", errors);
                results.pushMap(result)
            }
        }
        promise.resolve(results)
    }

    private fun getAnswers(questionAnswers: List<QuestionAnswer>): WritableArray {
        val answers = Arguments.createArray()
        for (answer in questionAnswers) {
            val result = Arguments.createMap().apply {
                putString("code", answer.code)
                putString("text", answer.text.get())
                putBoolean("isHeader", answer.isHeader)
            }
            val groupAnswer = Arguments.createArray()
            for (innerAnswer in answer.answers) {
                groupAnswer.pushMap(Arguments.createMap().apply {
                    putString("code", innerAnswer.code)
                    putString("text", innerAnswer.text.get())
                    putBoolean("isHeader", innerAnswer.isHeader)
                })
            }

            result.putArray("answers", groupAnswer)
            answers.pushMap(result)
        }
        return answers
    }

    // Question Control
    @ReactMethod
    fun setText(serverId: String, programKey: String, surveyId: String, questionId: String, answer: String, promise: Promise) {
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val text = it as? TextQuestion
            text?.setValue(answer)
        }
        promise.resolve(null)
    }

    @ReactMethod
    fun getText(serverId: String, programKey: String, surveyId: String, questionId: String, promise: Promise) {
        var result = ""
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val text = it as? TextQuestion
            result = text?.getValue() ?: ""
        }
        promise.resolve(result)
    }

    @ReactMethod
    fun setNumeric(serverId: String, programKey: String, surveyId: String, questionId: String, answer: Double, isDouble: Boolean, promise: Promise) {
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val text = it as? NumericQuestion
            if (isDouble) {
                text?.setValue(answer)
            } else {
                text?.setValue(answer.toInt())
            }

        }
        promise.resolve(null)
    }

    @ReactMethod
    fun getNumeric(serverId: String, programKey: String, surveyId: String, questionId: String, promise: Promise) {
        var result = ""
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val text = it as? NumericQuestion
            result = text?.getValue() ?: ""
        }
        promise.resolve(result)
    }

    @ReactMethod
    fun setSingle(serverId: String, programKey: String, surveyId: String, questionId: String, code: String, promise: Promise) {
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val single = it as? SingleQuestion
            val allAnswers = single?.answers?.flatMap { flatAnswer ->
                if (flatAnswer.isHeader) {
                    return@flatMap flatAnswer.answers
                }
                return@flatMap listOf(flatAnswer)
            }
            allAnswers?.firstOrNull { findAnswer -> findAnswer.code == code }?.let { questionAnswer ->
                single.select(questionAnswer)
            }
        }
        promise.resolve(null)
    }

    @ReactMethod
    fun getSingle(serverId: String, programKey: String, surveyId: String, questionId: String, promise: Promise) {
        val result = Arguments.createMap()
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val single = it as? SingleQuestion
            single?.selected()?.let { selected ->
                result.apply {
                    putString("code", selected.code)
                    putString("text", selected.text.get())
                    putBoolean("isHeader", selected.isHeader)
                }
            }
        }
        promise.resolve(result)
    }

    @ReactMethod
    fun setMulti(serverId: String, programKey: String, surveyId: String, questionId: String, code: String, selected: Boolean, promise: Promise) {
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            val multi = it as? MultiQuestion
            val allAnswers = multi?.answers?.flatMap { flatAnswer ->
                if (flatAnswer.isHeader) {
                    return@flatMap flatAnswer.answers
                }
                return@flatMap listOf(flatAnswer)
            }
            allAnswers?.firstOrNull { findAnswer -> findAnswer.code == code }?.let { questionAnswer ->
                multi.set(questionAnswer, selected)
            }
        }
        promise.resolve(null)
    }

    @ReactMethod
    fun getMulti(serverId: String, programKey: String, surveyId: String, questionId: String, promise: Promise) {
        val result = Arguments.createArray()
        surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.page?.questions?.firstOrNull { it.id == questionId }?.let {
            (it as? MultiQuestion)?.let { multi ->
                val allAnswers = multi.answers.flatMap { flatAnswer ->
                    if (flatAnswer.isHeader) {
                        return@flatMap flatAnswer.answers
                    }
                    return@flatMap listOf(flatAnswer)
                }
                for (answer in allAnswers) {
                    if (multi.get(answer)) {
                        result.pushMap(Arguments.createMap().apply {
                            putString("code", answer.code)
                            putString("text", answer.text.get())
                            putBoolean("isHeader", answer.isHeader)
                        })
                    }
                }
            }
        }
        promise.resolve(result)
    }

    // Survey Control
    @ReactMethod
    fun startSurvey(serverId: String, programKey: String, surveyId: String, data: ReadableMap, respondentValue: ReadableMap, promise: Promise) {
        val dataResult = mutableMapOf<String, String>()
        for (entry in data.entryIterator) {
            dataResult[entry.key] = entry.value.toString()
        }
        val respondentValueResult = mutableMapOf<String, String>()
        for (entry in respondentValue.entryIterator) {
            respondentValueResult[entry.key] = entry.value.toString()
        }

        val result = surveyManager.getSurvey(serverId, programKey, surveyId)?.startSurvey(dataResult, respondentValueResult)
        surveyActionResult(serverId, programKey, surveyId, promise, result)
    }

    @ReactMethod
    fun next(serverId: String, programKey: String, surveyId: String, promise: Promise) {
        val result = surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.next()
        surveyActionResult(serverId, programKey, surveyId, promise, result)
    }

    @ReactMethod
    fun back(serverId: String, programKey: String, surveyId: String, promise: Promise) {
        val result = surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.back()
        surveyActionResult(serverId, programKey, surveyId, promise, result)
    }

    @ReactMethod
    fun quit(serverId: String, programKey: String, surveyId: String, upload: Boolean, promise: Promise) {
        val result = surveyManager.getSurvey(serverId, programKey, surveyId)?.surveyFrame?.quit(upload)
        surveyActionResult(serverId, programKey, surveyId, promise, result)
    }

    private fun surveyActionResult(serverId: String, programKey: String, surveyId: String, promise: Promise, result: SurveyFrameActionResult?) {
        if (result == null) {
            promise.reject("survey", "failed to find active survey for ServerId: $serverId ProgramKey: $programKey SurveyId: $surveyId")
        } else {
            promise.resolve(Arguments.createMap().apply {
                putBoolean("success", result.success)
                putString("message", result.message)
            })
        }
    }
}