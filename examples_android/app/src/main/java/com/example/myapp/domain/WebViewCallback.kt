package com.example.myapp.domain

import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.web.SurveyWebViewFragmentCallback
import com.example.myapp.MainApplication
import com.example.myapp.utils.Utils

class WebViewCallback : SurveyWebViewFragmentCallback {
    override fun onWebViewSurveyError(serverId: String, projectId: String, exception: Exception) {
        val context = ConfirmitSDK.androidContext as MainApplication
        Utils.showError(context.activity!!, "WebView Survey Failed", exception.localizedMessage)
    }

    override fun onWebViewSurveyFinished(serverId: String, projectId: String) {
        // Do something
    }

    override fun onWebViewSurveyQuit(serverId: String, projectId: String) {
        // Do something
    }
}