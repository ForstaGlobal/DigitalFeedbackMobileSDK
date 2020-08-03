package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.SurveyLayout
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.domain.SurveyManager
import com.confirmit.testsdkapp.utils.Utils
import com.confirmit.testsdkapp.viewmodels.OverviewViewModel
import kotlinx.android.synthetic.main.fragment_survey_overview.*

enum class SurveyConfigType {
    LANGUAGE,
    SURVEY_LAYOUT,
    RESPONDENT_VALUE,
    REMOVE_SURVEY
}

class OverviewFragment : BaseFragment(), OnOverviewItemClicked, View.OnClickListener {

    override val layoutId: Int = R.layout.fragment_survey_overview

    private lateinit var surveyId: String
    private lateinit var serverId: String
    private lateinit var overviewAdapter: OverviewAdapter
    private lateinit var viewModel: OverviewViewModel

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        // Unload survey information
        val bundle = OverviewFragmentArgs.fromBundle(arguments!!)
        surveyId = bundle.surveyId
        serverId = bundle.serverId
        viewModel = OverviewViewModel(serverId, surveyId)

        val mainActivity = activity as MainActivity
        val fab = mainActivity.showFab()
        fab.setImageResource(R.drawable.ic_edit)
        fab.setOnClickListener(this)

        overviewAdapter = OverviewAdapter(viewModel, this)
        overviewTable.adapter = overviewAdapter
        overviewTable.layoutManager = LinearLayoutManager(context)
    }

    override fun onConfigClicked(config: SurveyConfigType) {
        val context = this.requireContext()
        when (config) {
            SurveyConfigType.LANGUAGE -> {
                var selected = viewModel.survey.survey.languages.indexOfFirst { it.id == viewModel.survey.selectedLanguage.id }
                AlertDialog.Builder(context)
                        .setTitle("Select Language")
                        .setSingleChoiceItems(viewModel.survey.survey.languages.map { it.name }.toTypedArray(), selected, null)
                        .setPositiveButton("OK") { dialog, _ ->
                            selected = (dialog as AlertDialog).listView.checkedItemPosition
                            viewModel.updateSelectedLanguage(viewModel.survey.survey.languages[selected].id)
                            overviewAdapter.notifyDataSetChanged()
                        }
                        .show()
            }
            SurveyConfigType.SURVEY_LAYOUT -> {
                var selected = SurveyLayout.toStringArray().indexOf(viewModel.survey.configs.surveyLayout.value)
                AlertDialog.Builder(context)
                        .setTitle("Select Layout")
                        .setSingleChoiceItems(R.array.layouts, selected, null)
                        .setPositiveButton("OK") { dialog, _ ->
                            selected = (dialog as AlertDialog).listView.checkedItemPosition
                            SurveyLayout.getValue(SurveyLayout.toStringArray()[selected])?.let {
                                viewModel.survey.configs.surveyLayout = it
                                overviewAdapter.notifyDataSetChanged()
                            }
                        }
                        .show()
            }
            SurveyConfigType.RESPONDENT_VALUE -> {
                val input = EditText(context)
                input.hint = "eg. <key>=<value>;"
                input.setSingleLine(false)
                input.setText(viewModel.survey.configs.respondentValue, TextView.BufferType.EDITABLE)

                val lp = LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                val margin = Utils.toPx(context, 16)
                lp.setMargins(margin, 0, margin, 0)
                input.layoutParams = lp

                val container = LinearLayout(context)
                container.addView(input)

                AlertDialog.Builder(context)
                        .setTitle("Enter Respondent Values")
                        .setView(container)
                        .setPositiveButton("OK") { _, _ ->
                            viewModel.survey.configs.respondentValue = input.text.toString()
                            overviewAdapter.notifyDataSetChanged()
                        }
                        .show()
            }
            SurveyConfigType.REMOVE_SURVEY -> {
                if (viewModel.removeSurvey()) {
                    findNavController().popBackStack()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        SurveyManager().launchSurvey(activity!!, viewModel.survey)
    }
}