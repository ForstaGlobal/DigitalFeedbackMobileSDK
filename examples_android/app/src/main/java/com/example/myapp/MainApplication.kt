package com.example.myapp

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.ConfirmitServer
import com.confirmit.mobilesdk.SurveySDK
import com.confirmit.mobilesdk.UniqueIdProvider
import com.example.myapp.domain.TriggerManager
import com.example.myapp.utils.Utils

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        var instance: MainApplication? = null
            private set

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

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
        return "My Custom Unique ID"
    }
}