package com.confirmit.testsdkapp.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.domain.SurveyManager
import com.confirmit.testsdkapp.domain.TriggerManager
import com.confirmit.testsdkapp.viewmodels.SurveyListViewModel
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel
import kotlinx.android.synthetic.main.fragment_survey_list.*

class SurveyListFragment : BaseFragment(), OnSurveyCardViewClicked, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    override var layoutId: Int = R.layout.fragment_survey_list

    private val viewModel: SurveyListViewModel = SurveyListViewModel()
    private lateinit var surveyListAdapter: SurveyListAdapter

    override fun initContentView(rootView: View, savedInstanceState: Bundle?) {
        hideKeyboard()

        val mainActivity = activity as MainActivity
        val fab = mainActivity.showFab()
        fab.setImageResource(R.drawable.ic_add_black)
        fab.setOnClickListener(this)

        surveyListAdapter = SurveyListAdapter(mainActivity, viewModel, this)

        surveyListNoSurvey.visibility = ConstraintLayout.GONE
        surveyListTable.adapter = surveyListAdapter
        surveyListTable.layoutManager = LinearLayoutManager(context)
        surveyListSwipeRefresh.setOnRefreshListener(this)
        surveyListSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        refreshTable()

        handleNewSurvey()
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadSurveys()
        downloadAll()
        refreshTable()
    }

    private fun downloadAll() {
        viewModel.downloadAllAsync {
            postUi {
                refreshTable()
                surveyListSwipeRefresh.isRefreshing = false
            }
        }
    }

    private fun handleNewSurvey() {
        if (arguments == null) {
            return
        }

        val bundle = SurveyListFragmentArgs.fromBundle(arguments!!)
        var serverId = bundle.newSurveyServerId
        val surveyId = bundle.newSurveySurveyId
        val clientId = bundle.newTriggerClientId
        val clientSecret = bundle.newTriggerClientSecret
        if (serverId.isBlank() || surveyId.isBlank()) {
            return
        }

        if (clientId.isNotEmpty() && clientSecret.isNotEmpty()) {
            serverId = viewModel.setNewServer(serverId, clientId, clientSecret).serverId
        }

        arguments!!.clear()
        arguments!!.putString("newSurveyServerId", "")
        arguments!!.putString("newSurveySurveyId", "")
        arguments!!.putString("newTriggerClientId", "")
        arguments!!.putString("newTriggerClientSecret", "")
        TriggerManager().setDelegate(serverId, surveyId)
        (activity as MainActivity).fetcherListener?.startIdle()
        viewModel.downloadAsync(serverId, surveyId) {
            postUi {
                refreshTable()
                (activity as MainActivity).fetcherListener?.endIdle()
            }
        }
    }

    private fun refreshTable() {
        postUi {
            surveyListNoSurvey.visibility = if (viewModel.surveys.isNotEmpty()) ConstraintLayout.GONE else ConstraintLayout.VISIBLE
            surveyListAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        findNavController().navigate(SurveyListFragmentDirections.actionSurveyListToAddSurvey())
    }

    override fun onRefresh() {
        downloadAll()
    }


    override fun onCardClicked(survey: SurveyModel) {
        findNavController().navigate(SurveyListFragmentDirections.actionSurveyListToOverview(survey.survey.surveyId, survey.survey.serverId))
    }

    override fun onStartClicked(survey: SurveyModel) {
        SurveyManager().launchSurvey(activity!!, survey)
    }
}



