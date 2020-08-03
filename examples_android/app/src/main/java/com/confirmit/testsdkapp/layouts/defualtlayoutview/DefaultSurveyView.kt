package com.confirmit.testsdkapp.layouts.defualtlayoutview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.ui.SurveyFrame
import com.confirmit.mobilesdk.ui.SurveyFrameConfig
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener
import com.confirmit.mobilesdk.ui.SurveyPage
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.MainApplication
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.fragments.SurveyLayoutOnCloseListener
import com.confirmit.testsdkapp.layouts.defaultlayout.DefaultSurveyAdapter
import com.confirmit.testsdkapp.layouts.defaultlayout.items.*
import com.confirmit.testsdkapp.ui.UiHandler
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

interface DefaultSurveyViewCallback {
    fun onSurveyClosed()
}

class DefaultSurveyView : LinearLayout, SurveyFrameLifecycleListener, DefaultLayoutSectionListener {
    private lateinit var containerSystem: ConstraintLayout
    private lateinit var btnClose: ImageButton
    private lateinit var btnFinish: Button
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button
    private lateinit var txtSystem: TextView
    private lateinit var titleSystem: TextView
    private lateinit var imgSystem: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var txtToolbar: TextView
    private lateinit var toolbar: ConstraintLayout

    private lateinit var adapter: DefaultSurveyAdapter

    private val sections = mutableListOf<DefaultLayoutSection>()

    private val surveyFrame = SurveyFrame()
    private lateinit var surveyModel: SurveyModel
    private var surveyFrameConfig: SurveyFrameConfig? = null

    private var onCloseListener: SurveyLayoutOnCloseListener? = null

    private val uiHandler: UiHandler = UiHandler()

    var listener: DefaultSurveyViewCallback? = null

    fun postUi(operation: () -> Unit) {
        uiHandler.postUi(operation)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initialize(surveyModel: SurveyModel, surveyFrameConfig: SurveyFrameConfig? = null) {
        this.surveyModel = surveyModel
        this.surveyFrameConfig = surveyFrameConfig

        val view = LayoutInflater.from(context).inflate(R.layout.view_default_survey, this)
        containerSystem = view.findViewById(R.id.containerSystem)
        txtToolbar = view.findViewById(R.id.toolbarTitle)
        toolbar = view.findViewById(R.id.toolbar)
        btnClose = view.findViewById(R.id.btnClose)
        btnFinish = view.findViewById(R.id.btnFinish)
        btnNext = view.findViewById(R.id.btnNext)
        btnBack = view.findViewById(R.id.btnBack)
        txtSystem = view.findViewById(R.id.txtSystem)
        titleSystem = view.findViewById(R.id.titleSystem)
        imgSystem = view.findViewById(R.id.imgSystem)
        recyclerView = view.findViewById(R.id.defaultSurveyRecyclerView)

        adapter = DefaultSurveyAdapter(sections)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        containerSystem.visibility = ConstraintLayout.GONE

        btnClose.setOnClickListener {
            surveyFrame.quit(AppConfigs.uploadIncomplete)
        }

        btnFinish.setOnClickListener {
            closeSurvey()
        }

        btnBack.setOnClickListener {
            hideKeyboard()
            surveyFrame.back()
        }

        btnNext.setOnClickListener {
            hideKeyboard()
            surveyFrame.next()
        }

        txtToolbar.text = surveyModel.survey.name

        loadFrame(this)
    }

    override fun onSurveyPageReady(page: SurveyPage) {
        sections.clear()
        var startIndex = 0
        if (!page.title.isEmpty()) {
            sections.add(DefaultLayoutPageSection(page))
            startIndex = 1
        }
        page.questions.forEachIndexed { index, question ->
            val section = DefaultLayoutQuestionSection(question)
            section.load(this, index + startIndex)
            sections.add(section)
        }
        adapter.notifyDataSetChanged()
        updateNavigationButton()

        val mainApp = ConfirmitSDK.androidContext as MainApplication
        (mainApp.activity as MainActivity).fetcherListener?.endIdle()
    }

    override fun onSurveyErrored(page: SurveyPage, values: Map<String, String?>, exception: Exception) {
        sections.clear()
        adapter.notifyDataSetChanged()
        updateNavigationSystemButton()

        containerSystem.visibility = ConstraintLayout.VISIBLE
        txtSystem.text = page.text!!.get()
        titleSystem.text = page.title.get()
        imgSystem.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyError))
        imgSystem.setImageResource(R.drawable.ic_sentiment_very_dissatisfied)
    }

    override fun onSurveyFinished(page: SurveyPage, values: Map<String, String?>) {
        sections.clear()
        adapter.notifyDataSetChanged()
        updateNavigationSystemButton()

        containerSystem.visibility = ConstraintLayout.VISIBLE
        titleSystem.text = page.title!!.get()
        txtSystem.text = page.text!!.get()
        imgSystem.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyButton))
        imgSystem.setImageResource(R.drawable.ic_thumb_up_alt_black)
    }

    override fun onSurveyQuit(values: Map<String, String?>) {
        listener?.onSurveyClosed()
    }

    override fun onReloadPage(add: List<IndexPath>, refresh: List<IndexPath>, remove: List<IndexPath>) {
        recyclerView.post {
            adapter.refresh()
            for (index in add) {
                adapter.notifyItemInserted(adapter.getItemIndex(index.section, index.row))
            }
            for (index in remove) {
                adapter.notifyItemRemoved(adapter.getItemIndex(index.section, index.row))
            }
            for (index in refresh) {
                adapter.notifyItemChanged(adapter.getItemIndex(index.section, index.row))
            }
        }
    }

    private fun loadFrame(surveyFrameLifecycleListener: SurveyFrameLifecycleListener) {
        try {
            if (surveyFrameConfig == null) {
                surveyFrameConfig = SurveyFrameConfig(surveyModel.survey.serverId, surveyModel.survey.surveyId)
            }

            surveyFrameConfig!!.languageId = surveyModel.selectedLanguage.id
            surveyFrameConfig!!.respondentValues = surveyModel.getRespondentValues()

            surveyFrame.load(surveyFrameConfig!!)
            surveyFrame.surveyFrameLifeCycleListener = surveyFrameLifecycleListener
            surveyFrame.start()
        } catch (e: Exception) {
            errorCloseSurvey(e)
        }
    }

    private fun closeSurvey() {
        listener?.onSurveyClosed()
    }

    private fun errorCloseSurvey(e: Exception) {
        postUi {
            listener?.onSurveyClosed()
            onCloseListener?.onCloseError("$e")
        }
    }

    private fun updateNavigationSystemButton() {
        btnBack.visibility = View.INVISIBLE
        btnNext.visibility = View.INVISIBLE
        btnFinish.visibility = View.VISIBLE
    }

    private fun updateNavigationButton() {
        btnBack.visibility = if (surveyFrame.page!!.showBackward) View.VISIBLE else View.INVISIBLE
        btnNext.visibility = if (surveyFrame.page!!.showForward) View.VISIBLE else View.INVISIBLE
        btnFinish.visibility = if (surveyFrame.page!!.showOk) View.VISIBLE else View.INVISIBLE
        btnBack.text = surveyFrame.page!!.backwardText
        btnNext.text = surveyFrame.page!!.forwardText
        btnFinish.text = surveyFrame.page!!.okText
    }

    fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(rootView.windowToken, 0)
    }
}