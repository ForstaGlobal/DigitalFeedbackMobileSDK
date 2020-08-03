package com.confirmit.testsdkapp.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.R
import com.confirmit.testsdkapp.activities.MainActivity
import com.confirmit.testsdkapp.viewmodels.TriggerOverviewViewModel
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel

interface OnTriggerOverviewItemClicked {
    fun onConfigClicked(config: ProgramConfigType)
}

abstract class TriggerOverviewItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setup(program: ProgramModel, config: ProgramConfigType?)
}

class TriggerOverviewCardViewHolder(view: View) : TriggerOverviewItemViewHolder(view) {
    private val txtName: TextView = view.findViewById(R.id.triggerOverviewCardName)

    override fun setup(program: ProgramModel, config: ProgramConfigType?) {
        txtName.text = program.program.programKey
    }
}

class TriggerOverviewDoubleConfigViewHolder(view: View, private val triggerOverviewClicked: OnTriggerOverviewItemClicked) : TriggerOverviewItemViewHolder(view), View.OnClickListener {
    private lateinit var configType: ProgramConfigType
    private val title: TextView = view.findViewById(R.id.txtTitle)
    private val secondary: TextView = view.findViewById(R.id.txtSubtitle)
    private val imgButton: AppCompatImageButton = view.findViewById(R.id.btnImage)

    init {
        view.isClickable = true
        view.setOnClickListener(this)
    }

    override fun setup(program: ProgramModel, config: ProgramConfigType?) {
        this.configType = config!!

        when (config) {
            ProgramConfigType.CUSTOM_DATA -> {
                title.text = "Custom Data Value (eg. <key>=<value>;"
                secondary.text = if (program.configs.customData.isEmpty()) "(Not Set)" else program.configs.customData
                imgButton.setImageResource(R.drawable.baseline_vpn_key_black_24)
            }
            ProgramConfigType.ALLOW_OVERLAY -> {
                title.text = "Allow Overlay Trigger"
                secondary.text = if (AppConfigs.enabledOverlayProgram == program.configs.programKey) "enabled" else "disabled"
                imgButton.setImageResource(R.drawable.round_content_paste_24)
            }
            else -> {
            }
        }
    }

    override fun onClick(p0: View?) {
        triggerOverviewClicked.onConfigClicked(configType)
    }
}

class TriggerOverviewSingleConfigViewHolder(private val mainActivity: MainActivity, private val view: View, private val triggerOverviewItemClicked: OnTriggerOverviewItemClicked) : TriggerOverviewItemViewHolder(view), View.OnClickListener {
    private lateinit var configType: ProgramConfigType
    private val title: TextView = view.findViewById(R.id.txtLabel)
    private val imgIcon: ImageView = view.findViewById(R.id.imgIcon)

    init {
        view.isClickable = true
        view.setOnClickListener(this)
    }

    override fun setup(program: ProgramModel, config: ProgramConfigType?) {
        this.configType = config!!

        when (configType) {
            ProgramConfigType.ON_EVENT_TRIGGER -> {
                title.text = "Trigger onTestEvent"
                imgIcon.setImageResource(R.drawable.ic_assignment_black)
                view.contentDescription = "trigger_event"
            }
            ProgramConfigType.REMOVE_PROGRAM -> {
                title.text = "Remove Program"
                title.setTextColor(ContextCompat.getColor(title.context, R.color.error))
                imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.context, R.color.icon))
                imgIcon.setImageResource(R.drawable.baseline_delete_black_24)
            }
            else -> {
            }
        }
    }

    override fun onClick(p0: View?) {
        if (configType == ProgramConfigType.ON_EVENT_TRIGGER) {
            mainActivity.fetcherListener?.startIdle()
        }

        triggerOverviewItemClicked.onConfigClicked(configType)
    }
}

enum class TriggerOverviewViewType(val value: Int) {
    OVERVIEW(0),
    SINGLE(1),
    DOUBLE(2);

    companion object {
        fun fromValue(value: Int): TriggerOverviewViewType? = TriggerOverviewViewType.values().find { it.value == value }
    }
}

class TriggerOverviewAdapter(private val mainActivity: MainActivity, private val viewModel: TriggerOverviewViewModel, private val itemClickedListener: OnTriggerOverviewItemClicked) : RecyclerView.Adapter<TriggerOverviewItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TriggerOverviewItemViewHolder {
        val type = TriggerOverviewViewType.fromValue(viewType)!!
        return when (type) {
            TriggerOverviewViewType.OVERVIEW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_overview_card_trigger, parent, false)
                TriggerOverviewCardViewHolder(view)
            }
            TriggerOverviewViewType.SINGLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_single_line, parent, false)
                TriggerOverviewSingleConfigViewHolder(mainActivity, view, itemClickedListener)
            }
            TriggerOverviewViewType.DOUBLE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.app_cell_item_two_line, parent, false)
                TriggerOverviewDoubleConfigViewHolder(view, itemClickedListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            0 -> {
                return TriggerOverviewViewType.OVERVIEW.value
            }
            else -> {
                val config = ProgramConfigType.values()[position - 1]
                return when (config) {
                    ProgramConfigType.REMOVE_PROGRAM, ProgramConfigType.ON_EVENT_TRIGGER -> {
                        TriggerOverviewViewType.SINGLE.value
                    }
                    else -> {
                        TriggerOverviewViewType.DOUBLE.value
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TriggerOverviewItemViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.setup(viewModel.program, null)
            }
            else -> {
                val config = ProgramConfigType.values()[position - 1]
                holder.setup(viewModel.program, config)
            }
        }
    }

    override fun getItemCount(): Int {
        return ProgramConfigType.values().size + 1
    }
}