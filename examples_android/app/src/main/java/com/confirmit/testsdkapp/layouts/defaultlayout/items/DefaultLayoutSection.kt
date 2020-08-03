package com.confirmit.testsdkapp.layouts.defaultlayout.items

import com.confirmit.mobilesdk.ui.SurveyPage
import com.confirmit.mobilesdk.ui.ValidationQuestionError
import com.confirmit.mobilesdk.ui.questions.*
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.layouts.defaultlayout.cells.DefaultLayoutCellChangeListener

interface DefaultLayoutItem

interface DefaultLayoutSectionListener {
    fun onReloadPage(add: List<IndexPath>, refresh: List<IndexPath>, remove: List<IndexPath>)
}

interface DefaultLayoutSection : DefaultLayoutCellChangeListener {
    var items: MutableList<DefaultLayoutItem>
    fun load(listener: DefaultLayoutSectionListener, section: Int)
}

class DefaultLayoutPageSection(page: SurveyPage) : DefaultLayoutSection {
    override var items: MutableList<DefaultLayoutItem> = mutableListOf()

    init {
        items = DefaultLayoutItemFactory.createPageHeader(page)
    }

    override fun load(listener: DefaultLayoutSectionListener, section: Int) {
    }

    override fun onTextUpdated(text: String) {
    }

    override fun onRadioClicked(answer: QuestionAnswer) {
    }

    override fun onCheckClicked(answer: QuestionAnswer, selected: Boolean) {
    }

    override fun getSelected(answer: QuestionAnswer): Boolean {
        return false
    }

}

class DefaultLayoutQuestionSection(private val question: Question) : DefaultLayoutSection {
    private var listener: DefaultLayoutSectionListener? = null

    override var items: MutableList<DefaultLayoutItem> = mutableListOf()
    private var section: Int = -1

    init {
        items = createItems(question)
    }

    override fun load(listener: DefaultLayoutSectionListener, section: Int) {
        this.listener = listener
        this.section = section
    }

    private fun createItems(question: Question): MutableList<DefaultLayoutItem> {
        return when (question) {
            is InfoQuestion -> DefaultLayoutItemFactory.createInfo(question)
            is TextQuestion -> DefaultLayoutItemFactory.createText(question)
            is NumericQuestion -> DefaultLayoutItemFactory.createNumeric(question)
            is SingleQuestion -> DefaultLayoutItemFactory.createSingle(question)
            is MultiQuestion -> DefaultLayoutItemFactory.createMulti(question)
            else -> DefaultLayoutItemFactory.createNotSupported()
        }
    }

    private fun updateValidation(question: DefaultQuestion, errors: List<ValidationQuestionError>) {
        val validationIndex = DefaultLayoutItemUtils.getValidationIndex(section, question)
        val item = items.getOrNull(validationIndex.row) ?: return

        val iconLabelItem = item as? DefaultLayoutIconLabel

        if (iconLabelItem == null) {
            if (errors.isNotEmpty()) {
                items.add(validationIndex.row, DefaultLayoutIconLabel(errors))
                listener?.onReloadPage(mutableListOf(validationIndex), mutableListOf(), mutableListOf())
            }
            return
        }

        val addRow: MutableList<IndexPath> = mutableListOf()
        val removeRow: MutableList<IndexPath> = mutableListOf()
        val refreshRow: MutableList<IndexPath> = mutableListOf()

        if (iconLabelItem.errors.isNotEmpty()) {
            if (errors.isEmpty()) {
                items.removeAt(validationIndex.row)
                removeRow.add(validationIndex)
            } else {
                iconLabelItem.errors = errors
                refreshRow.add(validationIndex)
            }
        } else {
            items.add(validationIndex.row, DefaultLayoutIconLabel(errors))
            addRow.add(validationIndex)
        }

        listener?.onReloadPage(addRow, refreshRow, removeRow)
    }

    override fun onTextUpdated(text: String) {
        (question as? TextQuestion)?.let {
            if (it.getValue() != text) {
                it.setValue(text)
                validate(question)
            }
        }

        (question as? NumericQuestion)?.let {
            val parsed = text.toDoubleOrNull()
            it.setValue(parsed)
            validate(question)
        }
    }

    override fun onRadioClicked(answer: QuestionAnswer) {
        (question as? SingleQuestion)?.let {
            it.select(answer)

            validate(question)

            listener?.onReloadPage(mutableListOf(), DefaultLayoutItemUtils.getSingleAnswerIndexList(section, question, answer.code), mutableListOf())
        }
    }

    override fun onCheckClicked(answer: QuestionAnswer, selected: Boolean) {
        (question as? MultiQuestion)?.let {
            it.set(answer, selected)

            validate(question)
        }
    }

    override fun getSelected(answer: QuestionAnswer): Boolean {
        (question as? SingleQuestion)?.let {
            question.selected()?.let { selected ->
                return selected.code == answer.code
            }
            return false
        }

        (question as? MultiQuestion)?.let {
            return it.get(answer)
        }

        return false
    }

    private fun validate(question: DefaultQuestion) {
        if (AppConfigs.onPageQuestionValidation) {
            // Quick implementation try / catch is better practice
            val errors = question.validate()
            updateValidation(question, errors)
        }
    }

}
