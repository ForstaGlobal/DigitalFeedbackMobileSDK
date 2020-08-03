package com.confirmit.testsdkapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.viewmodels.OverviewViewModel
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel

interface OnOverviewItemClicked {
    fun onConfigClicked(config: SurveyConfigType)
}

abstract class OverviewItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setup(survey: SurveyModel, configType: SurveyConfigType?)
}

class OverviewCardViewHolder(view: View) : OverviewItemViewHolder(view) {
    private val txtName: TextView = view.findViewById(R.id.overviewCardName)
    private val txtSurveyId: TextView = view.findViewById(R.id.overviewCardSurveyId)
    private val txtDesc: TextView = view.findViewById(R.id.overviewCardDesc)

    override fun setup(survey: SurveyModel, configType: SurveyConfigType?) {
        txtName.text = survey.survey.name
        txtDesc.text = if (survey.survey.description.isEmpty()) "(No description)" else survey.survey.description
        txtSurveyId.text = survey.survey.surveyId
    }
}


class OverviewDoubleConfigViewHolder(view: View, private val overviewItemClicked: OnOverviewItemClicked) : OverviewItemViewHolder(view), View.OnClickListener {
    private lateinit var configType: SurveyConfigType
    private val title: TextView = view.findViewById(R.id.txtTitle)
    private val secondary: TextView = view.findViewById(R.id.txtSubtitle)
    private val imgButton: AppCompatImageButton = view.findViewById(R.id.btnImage)

    init {
        view.isClickable = true
        view.setOnClickListener(this)
    }

    override fun setup(survey: SurveyModel, configType: SurveyConfigType?) {
        this.configType = configType!!

        when (configType) {
            SurveyConfigType.LANGUAGE -> {
                title.text = "Language"
                secondary.text = "${survey.selectedLanguage.nativeName} (${survey.selectedLanguage.name})"
                imgButton.setImageResource(R.drawable.ic_language)
            }
            SurveyConfigType.SURVEY_LAYOUT -> {
                title.text = "Survey Layout"
                secondary.text = survey.configs.surveyLayout.value
                imgButton.setImageResource(R.drawable.ic_view_quilt)
            }
            SurveyConfigType.RESPONDENT_VALUE -> {
                title.text = "Respondent Value (eg. <key>=<value>;)"
                secondary.text = if (survey.configs.respondentValue.isEmpty()) "(Not Set)" else survey.configs.respondentValue
                imgButton.setImageResource(R.drawable.baseline_vpn_key_black_24)
            }
            else -> {
            }
        }
    }

    override fun onClick(view: View?) {
        overviewItemClicked.onConfigClicked(configType)
    }
}

class OverviewSingleConfigViewHolder(view: View, private val overviewItemClicked: OnOverviewItemClicked) : OverviewItemViewHolder(view), View.OnClickListener {
    private lateinit var configType: SurveyConfigType
    private val title: TextView = view.findViewById(R.id.txtLabel)
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

    init {
        view.isClickable = true
        view.setOnClickListener(this)
    }

    override fun setup(survey: SurveyModel, configType: SurveyConfigType?) {
        this.configType = configType!!

        when (configType) {
            SurveyConfigType.REMOVE_SURVEY -> {
                title.text = "Remove Survey"
                title.setTextColor(ContextCompat.getColor(title.context, R.color.error))
                imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, R.color.icon))
                imgIcon.setImageResource(R.drawable.baseline_delete_black_24)
            }
            else -> {
            }
        }
    }

    override fun onClick(view: View?) {
        overviewItemClicked.onConfigClicked(configType)
    }
}

enum class OverviewViewType(val value: Int) {
    OVERVIEW(0),
    SINGLE(1),
    DOUBLE(2);

    companion object {
        fun fromValue(value: Int): OverviewViewType? = OverviewViewType.values().find { it.value == value }
    }
}

class OverviewAdapter(private val viewModel: OverviewViewModel, private val itemClickListener: OnOverviewItemClicked) : RecyclerView.Adapter<OverviewItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewItemViewHolder {
        val type = OverviewViewType.fromValue(viewType)!!
        return when (type) {
            OverviewViewType.OVERVIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_overview_card_survey, parent, false)
                OverviewCardViewHolder(view)
            }
            OverviewViewType.SINGLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_single_line, parent, false)
                OverviewSingleConfigViewHolder(view, itemClickListener)
            }
            OverviewViewType.DOUBLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_two_line, parent, false)
                OverviewDoubleConfigViewHolder(view, itemClickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> {
                return OverviewViewType.OVERVIEW.value
            }
            else -> {
                val config = SurveyConfigType.values()[position - 1]
                return when (config) {
                    SurveyConfigType.REMOVE_SURVEY -> {
                        OverviewViewType.SINGLE.value
                    }
                    else -> {
                        OverviewViewType.DOUBLE.value
                    }
                }

            }
        }
    }

    override fun onBindViewHolder(holder: OverviewItemViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.setup(viewModel.survey, null)
            }
            else -> {
                val config = SurveyConfigType.values()[position - 1]
                holder.setup(viewModel.survey, config)
            }
        }
    }

    override fun getItemCount(): Int {
        return SurveyConfigType.values().size + 1
    }
}
