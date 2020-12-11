package com.example.myapp.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import kotlin.math.abs
import kotlin.math.min

fun Long.lighter(percentage: Float = 30f): Long {
    return this.adjustColor(abs(percentage))
}

fun Long.darker(percentag: Float = 30f): Long {
    return this.adjustColor(-1 * abs(percentag))
}

fun Long.adjustColor(percentage: Float = 30.0f): Long {
    val hA = this.shr(24) and 0xff
    val hR = this.shr(16) and 0xff
    val hG = this.shr(8) and 0xff
    val hB = this and 0xff

    val red = min(hR.toInt() + (255 * percentage / 100), 255f).toLong()
    val green = min(hG.toInt() + (255 * percentage / 100), 255f).toLong()
    val blue = min(hB.toInt() + (255 * percentage / 100), 255f).toLong()

    return hA.shl(24) or red.shl(16) or green.shl(8) or blue
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
}