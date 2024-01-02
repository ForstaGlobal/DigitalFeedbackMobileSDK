package com.mobilesdk.module

import android.webkit.JavascriptInterface
import android.webkit.WebView

fun WebView.enableSurveyAutoDismiss(onSurveyClosed: () -> Unit) {
    class MobileBridge {
        @JavascriptInterface
        fun onSurveyEnd() {
            onSurveyClosed()
        }
    }

    addJavascriptInterface(MobileBridge(), "mobileBridge")
}
