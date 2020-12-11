package com.example.myapp.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog

object Utils {
    lateinit var preferences: SharedPreferences

    fun initSharedPref(application: Application) {
        preferences = application.getSharedPreferences("AppConfigs", Context.MODE_PRIVATE)
    }

    fun showError(activity: Activity, title: String, message: String, completion: (() -> Unit)? = null) {
        activity.runOnUiThread {
            AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { _, _ ->
                    completion?.invoke()
                }
                .show()
        }
    }


    inline fun <reified T> getSetting(key: String, def: T): T {
        when (T::class) {
            Boolean::class -> {
                return preferences.getBoolean(key, def as Boolean) as T
            }
            else -> {
                preferences.getString(key, def as String)?.let {
                    return it as T
                }
            }
        }

        setSetting(key, def)
        return def
    }

    inline fun <reified T> setSetting(key: String, value: T) {
        val editor = preferences.edit()
        when (T::class) {
            Boolean::class -> editor.putBoolean(key, value as Boolean)
            else -> editor.putString(key, value as String)
        }
        editor.apply()
    }

    fun toPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

}