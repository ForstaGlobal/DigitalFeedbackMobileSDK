package com.confirmit.testsdkapp.layouts.defaultlayout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.layouts.defaultlayout.cells.*
import com.confirmit.testsdkapp.layouts.defaultlayout.items.*

class DefaultSurveySectionInfo(val index: Int, private val minRowIndex: Int, private val maxRowIndex: Int, val rowCount: Int) {
    fun isInSection(position: Int): Boolean {
        return position in minRowIndex..(maxRowIndex - 1)
    }

    fun getItemIndex(position: Int): Int {
        return position - minRowIndex
    }

    fun getItemAbsoluteIndex(row: Int): Int {
        return minRowIndex + row
    }
}

enum class DefaultSurveyCellType(val value: Int) {
    PAGE_HEADER(0),
    LABEL(1),
    ICON_LABEL(2),
    TEXTBOX(3),
    CLICK_LABEL(4);

    companion object {
        fun fromValue(value: Int): DefaultSurveyCellType? = DefaultSurveyCellType.values().find { it.value == value }
    }
}

class DefaultSurveyAdapter(private var sections: MutableList<DefaultLayoutSection>) : RecyclerView.Adapter<DefaultLayoutCell>() {
    private val sectionPositions = mutableListOf<Int>()
    private val sectionInfos = mutableListOf<DefaultSurveySectionInfo>()

    fun refresh() {
        itemCount // Reload section Infos
    }

    fun getItemIndex(section: Int, row: Int): Int {
        return sectionInfos[section].getItemAbsoluteIndex(row)
    }

    override fun getItemViewType(position: Int): Int {
        if (sectionPositions.contains(position)) {
            return DefaultSurveyCellType.PAGE_HEADER.value
        } else {
            sectionInfos.find { it.isInSection(position) }?.let {
                val section = sections[it.index]
                val item = section.items[it.getItemIndex(position)]
                when (item) {
                    is DefaultLayoutLabel -> {
                        return DefaultSurveyCellType.LABEL.value
                    }
                    is DefaultLayoutIconLabel -> {
                        return DefaultSurveyCellType.ICON_LABEL.value
                    }
                    is DefaultLayoutTextBox -> {
                        return DefaultSurveyCellType.TEXTBOX.value
                    }
                    is DefaultLayoutClickLabel -> {
                        return DefaultSurveyCellType.CLICK_LABEL.value
                    }
                    else -> {
                        return DefaultSurveyCellType.PAGE_HEADER.value
                    }
                }
            }
        }

        return DefaultSurveyCellType.PAGE_HEADER.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultLayoutCell {
        val type = DefaultSurveyCellType.fromValue(viewType)!!
        when (type) {
            DefaultSurveyCellType.PAGE_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.default_layout_section_header, parent, false)
                return DefaultLayoutSeparatorCell(view)
            }
            DefaultSurveyCellType.LABEL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.default_layout_label, parent, false)
                return DefaultLayoutLabelCell(view)
            }
            DefaultSurveyCellType.ICON_LABEL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.default_layout_icon_label, parent, false)
                return DefaultLayoutIconLabelCell(view)
            }
            DefaultSurveyCellType.TEXTBOX -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.default_layout_textbox, parent, false)
                return DefaultLayoutTextBoxCell(view)
            }
            DefaultSurveyCellType.CLICK_LABEL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.default_layout_click_label, parent, false)
                return DefaultLayoutClickLabelCell(view)
            }
        }
    }

    override fun onBindViewHolder(holder: DefaultLayoutCell, position: Int) {
        sectionInfos.find { it.isInSection(position) }?.let {
            val section = sections[it.index]
            val item = section.items[it.getItemIndex(position)]
            holder.setup(item, section)
        }
    }

    override fun getItemCount(): Int {
        sectionPositions.clear()
        sectionInfos.clear()

        var count = 0
        var minRowIndex = 1
        var maxRowIndex: Int
        val totalSectionCount = getTotalSectionCount()

        for (section in 0 until totalSectionCount) {
            val rowCount = getSectionItemCount(section)
            maxRowIndex = minRowIndex + rowCount
            sectionPositions.add(section, minRowIndex - 1)
            sectionInfos.add(section, DefaultSurveySectionInfo(section, minRowIndex, maxRowIndex, rowCount))

            count += rowCount

            minRowIndex = maxRowIndex + 1
        }

        return count + totalSectionCount
    }

    private fun getTotalSectionCount(): Int {
        return sections.count()
    }

    private fun getSectionItemCount(section: Int): Int {
        return sections[section].items.count()
    }
}

