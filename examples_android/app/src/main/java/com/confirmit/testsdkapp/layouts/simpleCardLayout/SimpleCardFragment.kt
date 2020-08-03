package com.confirmit.testsdkapp.layouts.simplecardlayout

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.viewpager.widget.ViewPager
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.confirmit.mobilesdk.ui.SurveyPage
import com.confirmit.mobilesdk.ui.questions.MultiQuestion
import com.confirmit.mobilesdk.ui.questions.NumericQuestion
import com.confirmit.mobilesdk.ui.questions.SingleQuestion
import com.confirmit.mobilesdk.ui.questions.TextQuestion
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.fragments.BaseSurveyFragment
import com.confirmit.testsdkapp.layouts.simplecardlayout.items.*

class SimpleCardFragment : BaseSurveyFragment(), SurveyFrameLifecycleListener, SimpleCardItemClickListeners {

    override var layoutId: Int = R.layout.fragment_simple_card_layout

    private lateinit var btnClose: AppCompatImageButton
    private lateinit var adapter: SimpleCardAdapter
    private lateinit var viewPager: ViewPager

    private val itemList: MutableList<SimpleCardItem> = mutableListOf()

    override fun initContentView(rootView: View, inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnClose = rootView.findViewById(R.id.btnClose)
        viewPager = rootView.findViewById(R.id.viewPager)

        btnClose.setOnClickListener {
            surveyFrame.quit(AppConfigs.uploadIncomplete)
        }

        loadFrame(this)
    }

    override fun onSurveyPageReady(page: SurveyPage) {
        itemList.clear()
        page.questions.forEachIndexed { index, question ->
            var cardItem: SimpleCardItem? = null
            when (question) {
                is TextQuestion -> {
                    cardItem = SimpleCardTextItem(LayoutInflater.from(requireContext()).inflate(R.layout.simple_card_text_item, null))
                    cardItem.setupQuestion(question)
                }
                is NumericQuestion -> {
                    cardItem = SimpleCardNumericItem(LayoutInflater.from(requireContext()).inflate(R.layout.simple_card_numeric_item, null))
                    cardItem.setupQuestion(question)
                }
                is SingleQuestion -> {
                    cardItem = SimpleCardSingleItem(LayoutInflater.from(requireContext()).inflate(R.layout.simple_card_single_item, null))
                    cardItem.setupQuestion(question)
                }
                is MultiQuestion -> {
                    cardItem = SimpleCardMultiItem(LayoutInflater.from(requireContext()).inflate(R.layout.simple_card_multi_item, null))
                    cardItem.setupQuestion(question)
                }
            }

            if (cardItem == null) {
                errorCloseSurvey("Simple Card layout only supported with [Text, Numeric, Single, Multi] questions.")
                return
            }

            cardItem.setupItem(
                    index,
                    showBack(page, index),
                    showNext(page, index),
                    index == 0,
                    index == page.questions.size - 1
            )
            cardItem.listener = this
            itemList.add(cardItem)
        }

        adapter = SimpleCardAdapter(itemList)
        viewPager.adapter = adapter
    }

    override fun onSurveyErrored(page: SurveyPage, values: Map<String, String?>, exception: Exception) {
        errorCloseSurvey(exception)
    }

    override fun onSurveyFinished(page: SurveyPage, values: Map<String, String?>) {
        closeSurvey()
    }

    override fun onSurveyQuit(values: Map<String, String?>) {
        closeSurvey()
    }

    override fun onBackClicked(index: Int) {
        if (index <= 0) {
            surveyFrame.back()
        } else {
            val newIndex = index - 1
            if (itemList[newIndex].onShow()) {
                hideKeyboard()
            }
            scrollTo(newIndex)
        }
    }

    override fun onNextClicked(index: Int) {
        if (index >= itemList.size - 1) {
            surveyFrame.next()
        } else {
            val newIndex = index + 1
            if (itemList[newIndex].onShow()) {
                hideKeyboard()
            }
            scrollTo(newIndex)
        }
    }

    private fun showBack(page: SurveyPage, index: Int): Boolean {
        if (index == 0) {
            return page.showBackward
        }

        return true
    }

    private fun showNext(page: SurveyPage, index: Int): Boolean {
        if (index == 0) {
            return page.showForward
        }

        return true
    }

    private fun scrollTo(index: Int) {
        viewPager.currentItem = index
    }
}