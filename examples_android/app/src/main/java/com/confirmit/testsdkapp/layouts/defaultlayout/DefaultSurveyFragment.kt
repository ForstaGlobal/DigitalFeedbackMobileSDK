package com.confirmit.testsdkapp.layouts.defaultlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.fragments.BaseSurveyFragment
import com.confirmit.testsdkapp.layouts.defualtlayoutview.DefaultSurveyView
import com.confirmit.testsdkapp.layouts.defualtlayoutview.DefaultSurveyViewCallback

class DefaultSurveyFragment : BaseSurveyFragment(), DefaultSurveyViewCallback {
    override var layoutId: Int = R.layout.fragment_default_layout

    private lateinit var adapter: DefaultSurveyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AlertDialogTheme)
    }

    override fun initContentView(rootView: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
        val surveyView = DefaultSurveyView(requireContext())
        val fragmentContainer: LinearLayout = rootView.findViewById(R.id.container)
        surveyView.initialize(surveyModel, surveyFrameConfig)
        surveyView.listener = this
        fragmentContainer.addView(surveyView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
    }

    override fun onSurveyClosed() {
        closeSurvey()
    }
}