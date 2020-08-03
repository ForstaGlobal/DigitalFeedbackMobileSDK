package com.confirmit.testsdkapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.confirmit.testsdkapp.ui.UiHandler

abstract class BaseFragment : Fragment() {
    protected abstract val layoutId: Int

    private val uiHandler: UiHandler = UiHandler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiHandler.createHandler()
        initContentView(view, savedInstanceState)
    }

    override fun onDestroyView() {
        uiHandler.releaseHandler()
        super.onDestroyView()
    }

    abstract fun initContentView(rootView: View, savedInstanceState: Bundle?)

    fun postUi(operation: () -> Unit) {
        uiHandler.postUi(operation)
    }

    fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.rootView.windowToken, 0)
    }
}