package com.mobilesdk

import android.R
import android.app.Application
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.ConfirmitServer
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Server
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.uimanager.util.ReactFindViewUtil
import com.mobilesdk.module.TriggerCallback
import com.mobilesdk.module.enableSurveyAutoDismiss
import com.reactnativecommunity.webview.RNCWebViewWrapper

class MobileSdkModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "MobileSdk"

        var application: Application? = null
    }

    override fun getName(): String {
        return NAME
    }

    @ReactMethod
    fun injectWebView() {
        Handler(reactApplicationContext.mainLooper).post {
            val viewGroup =
                (reactApplicationContext.currentActivity?.findViewById<View>(R.id.content) as ViewGroup).getChildAt(
                    0
                ) as ViewGroup
            val reactWebView =
                ReactFindViewUtil.findView(viewGroup, "surveyWebViews") as? RNCWebViewWrapper
            reactWebView?.webView?.enableSurveyAutoDismiss {
                reactApplicationContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit("__mobileOnSurveyClosed", Arguments.createMap())
            }
        }
    }


    @ReactMethod
    fun initSdk() {
        application?.let {
            ConfirmitSDK.Setup(it).configure()
        }
    }

    @ReactMethod
    fun enableLog(enable: Boolean) {
        ConfirmitSDK.enableLog(enable)
    }

    // Program
    @ReactMethod
    fun triggerDownload(serverId: String, programKey: String, promise: Promise) {
        val response = TriggerSDK.download(serverId, programKey)
        if (response.result) {
            promise.resolve(response.result)
        } else {
            promise.reject(response.exception)
        }
    }

    @ReactMethod
    fun deleteProgram(
        serverId: String,
        programKey: String,
        deleteCustomData: Boolean,
        promise: Promise
    ) {
        try {
            TriggerSDK.deleteProgram(serverId, programKey, deleteCustomData)
        } catch (error: Exception) {
            promise.reject(error)
        }
    }

    @ReactMethod
    fun deleteAll(deleteCustomData: Boolean, promise: Promise) {
        try {
            TriggerSDK.deleteAll(deleteCustomData)
        } catch (error: Exception) {
            promise.reject(error)
        }
    }

    @ReactMethod
    fun setCallback(serverId: String, programKey: String) {
        TriggerSDK.setCallback(
            serverId,
            programKey,
            TriggerCallback(reactApplicationContext, serverId, programKey)
        )
    }

    @ReactMethod
    fun removeCallback(serverId: String, programKey: String) {
        TriggerSDK.removeCallback(serverId, programKey)
    }

    @ReactMethod
    fun notifyEvent(event: String) {
        TriggerSDK.notifyEvent(event)
    }

    @ReactMethod
    fun notifyEventWithData(event: String, data: ReadableMap) {
        val result = mutableMapOf<String, String>()
        for (entry in data.entryIterator) {
            result[entry.key] = entry.value.toString()
        }
        TriggerSDK.notifyEvent(event, result)
    }

    @ReactMethod
    fun notifyAppForeground(data: ReadableMap) {
        val result = mutableMapOf<String, String>()
        for (entry in data.entryIterator) {
            result[entry.key] = entry.value.toString()
        }
        TriggerSDK.notifyAppForeground(result)
    }

    // Server
    @ReactMethod
    fun getUs(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.US))
    }

    @ReactMethod
    fun getUk(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.UK))
    }

    @ReactMethod
    fun getAustralia(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.AUSTRALIA))
    }

    @ReactMethod
    fun getCanada(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.CANADA))
    }

    @ReactMethod
    fun getGermany(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.GERMANY))
    }

    @ReactMethod
    fun getHxPlatform(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.HX_PLATFORM))
    }

    @ReactMethod
    fun getHxAustralia(promise: Promise) {
        promise.resolve(transformServer(ConfirmitServer.HX_AUSTRALIA))
    }

    @ReactMethod
    fun configureUs(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureUS(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure US")
        }
    }

    @ReactMethod
    fun configureUk(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureUK(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure UK")
        }
    }

    @ReactMethod
    fun configureAustralia(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureAustralia(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure Australia")
        }
    }

    @ReactMethod
    fun configureCanada(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureCanada(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure Canada")
        }
    }

    @ReactMethod
    fun configureGermany(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureGermany(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure Germany")
        }
    }

    @ReactMethod
    fun configureHxPlatform(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureHxPlatform(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure HX Platform")
        }
    }

    @ReactMethod
    fun configureHxAustralia(clientId: String, clientSecret: String, promise: Promise) {
        try {
            ConfirmitServer.configureHxAustralia(clientId, clientSecret)
            promise.resolve(null)
        } catch (ex: Exception) {
            promise.reject("server", "Failed to configure HX Australia")
        }
    }

    @ReactMethod
    fun configureServer(
        name: String,
        host: String,
        clientId: String,
        clientSecret: String,
        promise: Promise
    ) {
        val server = ConfirmitServer.configure(name, host, clientId, clientSecret)
        promise.resolve(transformServer(server))
    }

    @ReactMethod
    fun getServer(serverId: String, promise: Promise) {
        ConfirmitServer.getServer(serverId)?.let {
            promise.resolve(transformServer(it))
            return
        }
        promise.reject("server", "failed to get server")
    }

    @ReactMethod
    fun getServers(promise: Promise) {
        val servers = ConfirmitServer.getServers()
        val result = Arguments.createArray()
        for (server in servers) {
            result.pushMap(transformServer(server))
        }
        promise.resolve(result)
    }

    private fun transformServer(server: Server): WritableMap {
        return Arguments.createMap().apply {
            putString("host", server.host)
            putString("name", server.name)
            putString("serverId", server.serverId)
        }
    }
}
