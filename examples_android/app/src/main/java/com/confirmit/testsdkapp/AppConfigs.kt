package com.confirmit.testsdkapp

import com.confirmit.testsdkapp.utils.Utils
import java.io.Serializable
import java.util.*

object AppConfigs {
    var onDemandDownload: Boolean
        get() {
            return get(ConfigKey.ON_DEMAND_DOWNLOAD_PACKAGE, true)
        }
        set(value) {
            set(ConfigKey.ON_DEMAND_DOWNLOAD_PACKAGE, value)
        }


    var onPageQuestionValidation: Boolean
        get() {
            return get(ConfigKey.VALIDATE_ON_ANSWER_CHANGE, false)
        }
        set(value) {
            set(ConfigKey.VALIDATE_ON_ANSWER_CHANGE, value)
        }

    var uniqueId: String
        get() {
            return get(ConfigKey.UNIQUE_ID, UUID.randomUUID().toString())
        }
        set(value) {
            set(ConfigKey.UNIQUE_ID, value)
        }

    var htmlText: Boolean
        get() {
            return get(ConfigKey.HTML_TEXT, false)
        }
        set(value) {
            set(ConfigKey.HTML_TEXT, value)
        }

    var uploadIncomplete: Boolean
        get() {
            return get(ConfigKey.UPLOAD_INCOMPLETE, true)
        }
        set(value) {
            set(ConfigKey.UPLOAD_INCOMPLETE, value)
        }

    var triggerTestMode: Boolean
        get() {
            return get(ConfigKey.TRIGGER_TEST_MODE, false)
        }
        set(value) {
            set(ConfigKey.TRIGGER_TEST_MODE, value)
        }

    var enabledOverlayProgram: String
        get() {
            return get(ConfigKey.OVERLAY_ENABLED_PROGRAM, "")
        }
        set(value) {
            set(ConfigKey.OVERLAY_ENABLED_PROGRAM, value)
        }

    private inline fun <reified T> get(key: ConfigKey, def: T): T {
        return Utils.getSetting(key.name, def)
    }

    private inline fun <reified T> set(key: ConfigKey, value: T) {
        Utils.setSetting(key.name, value)
    }
}

class SurveyConfigs(val serverId: String, val surveyId: String) {
    var surveyLayout: SurveyLayout
        get() {
            return SurveyLayout.getValue(get(ConfigKey.SURVEY_LAYOUT, SurveyLayout.DEFAULT.value))
                    ?: SurveyLayout.DEFAULT
        }
        set(value) {
            set(ConfigKey.SURVEY_LAYOUT, value.value)
        }

    var respondentValue: String
        get() {
            return get(ConfigKey.RESPONDENT_VALUE, "")
        }
        set(value) {
            set(ConfigKey.RESPONDENT_VALUE, value)
        }

    private inline fun <reified T> get(key: ConfigKey, def: T): T {
        return Utils.getSetting(createKey(key), def)
    }

    private inline fun <reified T> set(key: ConfigKey, value: T) {
        Utils.setSetting(createKey(key), value)
    }

    private fun createKey(key: ConfigKey): String {
        return "${serverId}_${surveyId}_${key.name}"
    }
}

class ProgramConfig(val serverId: String, val programKey: String) : Serializable {
    var customData: String
        get() {
            return get(ConfigKey.CUSTOM_DATA, "")
        }
        set(value) {
            set(ConfigKey.CUSTOM_DATA, value)
        }

    private inline fun <reified T> get(key: ConfigKey, def: T): T {
        return Utils.getSetting(createKey(key), def)
    }

    private inline fun <reified T> set(key: ConfigKey, value: T) {
        Utils.setSetting(createKey(key), value)
    }

    private fun createKey(key: ConfigKey): String {
        return "${serverId}_${programKey}_${key.name}"
    }
}