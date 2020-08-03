package com.confirmit.testsdkapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.ui.UiHandler

abstract class BaseDialogFragment : DialogFragment() {
    private lateinit var rootView: View

    protected abstract var layoutId: Int

    private val uiHandler: UiHandler = UiHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        uiHandler.createHandler()
        rootView = inflater.inflate(layoutId, container, false)
        initContentView(rootView, inflater, container, savedInstanceState)

        return rootView
    }

    override fun onDestroyView() {
        uiHandler.releaseHandler()
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    abstract fun initContentView(rootView: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)

    fun postUi(operation: () -> Unit) {
        uiHandler.postUi(operation)
    }

    fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
    }
}