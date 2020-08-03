package com.confirmit.testsdkapp

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.confirmit.mobilesurveysdk.ConfirmitSDK
import com.confirmit.mobilesurveysdk.ConfirmitServer
import com.confirmit.mobilesurveysdk.SurveySDK
import com.confirmit.mobilesurveysdk.UniqueIdProvider
import com.confirmit.testsdkapp.domain.TriggerManager
import com.confirmit.testsdkapp.utils.Utils

class MainApplication : Application() {
    private var currentActivity: AppCompatActivity? = null

    var activity: AppCompatActivity?
        get() {
            return currentActivity
        }
        set(value) {
            currentActivity = value
        }

    override fun onCreate() {
        super.onCreate()

        Utils.initSharedPref(this)

        ConfirmitSDK.enableLog(true)
        ConfirmitSDK.Setup(this).configure()
        SurveySDK.setUniqueIdProvider(AppUniqueIdProvider())

        // TODO: Please enter your Client ID and Client Secret keys
        // Confirmit has a number of Horizons Servers. Please check your server, and initialize configuration
        // Example: UK Server
        ConfirmitServer.configureUK("<Client ID>", "<Client Secret>")

        TriggerManager().setAllDelegate()
    }
}

class AppUniqueIdProvider : UniqueIdProvider {
    override fun getUniqueId(): String {
        return AppConfigs.uniqueId
    }
}