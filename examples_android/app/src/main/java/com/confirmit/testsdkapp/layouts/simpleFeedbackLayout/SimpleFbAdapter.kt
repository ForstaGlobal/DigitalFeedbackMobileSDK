package com.confirmit.testsdkapp.layouts.simplefeedbacklayout

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class SimpleFbAdapter(val items: List<SimpleFbItem>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val page = items[position]
        container.addView(page.getView())
        return page.getView()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}