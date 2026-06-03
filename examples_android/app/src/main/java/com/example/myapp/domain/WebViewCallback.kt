package com.example.myapp.domain

import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.WebResourceErrorCompat
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

    override fun onWebViewError(serverId: String, projectId: String, view: WebView, request: WebResourceRequest, error: WebResourceErrorCompat) {
		// Do nothing
    }

    override fun onWebViewIntercept(serverId: String, projectId: String, view: WebView, url: String): Boolean {
		return false
    }
}