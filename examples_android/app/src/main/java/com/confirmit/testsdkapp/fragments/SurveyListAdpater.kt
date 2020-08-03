package com.confirmit.testsdkapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.viewmodels.SurveyListViewModel
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel
import com.google.android.material.card.MaterialCardView

interface OnSurveyCardViewClicked {
    fun onCardClicked(survey: SurveyModel)
    fun onStartClicked(survey: SurveyModel)
}

class SurveyListViewHolder(private val activity: MainActivity, view: View, private val cardClickListener: OnSurveyCardViewClicked) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private lateinit var survey: SurveyModel

    private val btnStart: Button = view.findViewById(R.id.surveyListCardStart)
    private val card: MaterialCardView = view.findViewById(R.id.surveyListCard)
    private val txtName: TextView = view.findViewById(R.id.surveyListCardName)
    private val txtSurveyId: TextView = view.findViewById(R.id.surveyListCardSurveyId)

    init {
        card.isClickable = true
        btnStart.setOnClickListener(this)
        card.setOnClickListener(this)
    }

    fun setup(survey: SurveyModel) {
        this.survey = survey

        txtName.text = survey.survey.name
        txtSurveyId.text = survey.survey.surveyId
        btnStart.contentDescription = "btnStart_${survey.survey.surveyId}"
    }

    override fun onClick(view: View?) {
        if (view == btnStart) {
            activity.fetcherListener?.startIdle()
            cardClickListener.onStartClicked(survey)
        }

        if (view == card) {
            cardClickListener.onCardClicked(survey)
        }
    }
}

class SurveyListAdapter(private val activity: MainActivity, private val viewModel: SurveyListViewModel, private val cardClickListener: OnSurveyCardViewClicked) : RecyclerView.Adapter<SurveyListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_card_survey, parent, false)
        return SurveyListViewHolder(activity, view, cardClickListener)
    }

    override fun getItemCount(): Int {
        return viewModel.surveys.count()
    }

    override fun onBindViewHolder(holder: SurveyListViewHolder, position: Int) {
        holder.setup(viewModel.surveys[position])
    }
}