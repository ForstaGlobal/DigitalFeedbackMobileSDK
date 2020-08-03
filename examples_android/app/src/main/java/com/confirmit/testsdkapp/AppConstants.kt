package com.confirmit.testsdkapp

enum class SurveyLayout(val value: String) {
    DEFAULT("Default"),
    DEFAULT_JAVA("Default Java"),
    CARD("Card"),
    SIMPLE_FEEDBACK("Simple Feedback");

    companion object {
        fun toStringArray(): List<String> {
            return mutableListOf(
                    DEFAULT.value,
                    DEFAULT_JAVA.value,
                    CARD.value,
                    SIMPLE_FEEDBACK.value
            )
        }

        fun getValue(value: String): SurveyLayout? = SurveyLayout.values().find {
            it.value == value
        }
    }
}

enum class ConfigKey {
    ON_DEMAND_DOWNLOAD_PACKAGE,
    VALIDATE_ON_ANSWER_CHANGE,
    UNIQUE_ID,
    HTML_TEXT,
    UPLOAD_INCOMPLETE,
    SURVEY_LAYOUT,
    RESPONDENT_VALUE,
    CUSTOM_DATA,
    TRIGGER_TEST_MODE,
    OVERLAY_ENABLED_PROGRAM
}