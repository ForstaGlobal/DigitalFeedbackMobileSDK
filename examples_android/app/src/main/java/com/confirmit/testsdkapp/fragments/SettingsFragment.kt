package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var downloadPref: CheckBoxPreference
    private lateinit var validationPref: CheckBoxPreference
    private lateinit var uniqueIdPref: EditTextPreference
    private lateinit var htmlTextPref: CheckBoxPreference
    private lateinit var uploadIncompletePref: CheckBoxPreference
    private lateinit var triggerTestModePref: CheckBoxPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.hideFab()

        downloadPref = preferenceManager.findPreference("key_download") as CheckBoxPreference
        validationPref = preferenceManager.findPreference("key_validation") as CheckBoxPreference
        uniqueIdPref = preferenceManager.findPreference("key_uniqueId") as EditTextPreference
        htmlTextPref = preferenceManager.findPreference("key_htmltext") as CheckBoxPreference
        uploadIncompletePref = preferenceManager.findPreference("key_uploadincomplete") as CheckBoxPreference
        triggerTestModePref = preferenceManager.findPreference("key_triggertestmode") as CheckBoxPreference

        downloadPref.isChecked = AppConfigs.onDemandDownload
        downloadPref.setOnPreferenceChangeListener { _, newValue ->
            AppConfigs.onDemandDownload = newValue as Boolean
            true
        }

        validationPref.isChecked = AppConfigs.onPageQuestionValidation
        validationPref.setOnPreferenceChangeListener { _, newValue ->
            AppConfigs.onPageQuestionValidation = newValue as Boolean
            true
        }

        uniqueIdPref.text = AppConfigs.uniqueId
        uniqueIdPref.summary = uniqueIdPref.text
        uniqueIdPref.setOnPreferenceChangeListener { pref, newValue ->
            AppConfigs.uniqueId = newValue as String
            pref.summary = AppConfigs.uniqueId
            true
        }

        htmlTextPref.isChecked = AppConfigs.htmlText
        htmlTextPref.setOnPreferenceChangeListener { _, newValue ->
            AppConfigs.htmlText = newValue as Boolean
            true
        }

        uploadIncompletePref.isChecked = AppConfigs.uploadIncomplete
        uploadIncompletePref.setOnPreferenceChangeListener { _, newValue ->
            AppConfigs.uploadIncomplete = newValue as Boolean
            true
        }

        triggerTestModePref.isChecked = AppConfigs.triggerTestMode
        triggerTestModePref.setOnPreferenceChangeListener { _, newValue ->
            AppConfigs.triggerTestMode = newValue as Boolean
            true
        }
    }
}