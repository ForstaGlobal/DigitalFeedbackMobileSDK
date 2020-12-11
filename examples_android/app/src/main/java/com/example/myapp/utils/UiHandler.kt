package com.example.myapp.utils

import android.os.Handler
import android.os.Looper

class UiHandler {
    private var uiHandler: Handler? = null

    fun createHandler() {
        synchronized(this) {
            if (uiHandler == null) {
                uiHandler = Handler(Looper.getMainLooper())
            }
        }
    }

    fun releaseHandler() {
        synchronized(this) {
            if (uiHandler != null) {
                uiHandler!!.removeCallbacksAndMessages(null)
                uiHandler = null
            }
        }
    }

    fun postUi(operation: () -> Unit) {
        synchronized(this) {
            if (uiHandler != null) {
                uiHandler!!.post(operation)
            }
        }
    }

    fun postUi(operation: () -> Unit, delay: Long) {
        synchronized(this) {
            if (uiHandler != null) {
                uiHandler!!.postAtTime(operation, delay)
            }
        }
    }
}